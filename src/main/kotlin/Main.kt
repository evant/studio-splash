package me.tatarka.android

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import me.tongfei.progressbar.ConsoleProgressBarConsumer
import me.tongfei.progressbar.ProgressBar
import me.tongfei.progressbar.ProgressBarBuilder
import me.tongfei.progressbar.wrapped.ProgressBarWrappedInputStream
import org.apache.commons.compress.archivers.ArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import java.io.IOException
import java.net.URL
import java.nio.file.Path
import java.util.zip.ZipEntry
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.outputStream

const val STUDIO_ARCHIVES = "https://redirector.gvt1.com/edgedl/android/studio/ide-zips/"

fun main() {
    val versions = readVersions()
    val outputDir = Path.of("./images/")
    outputDir.createDirectories()

    for (version in versions) {
        val imagePath = outputDir.resolve("${version.version}-${version.name}-${version.track}.png")
        if (!imagePath.exists()) {
            val url =
                URL(STUDIO_ARCHIVES + version.version + "/android-studio-" + version.version + "-linux.tar.gz")
            try {
                download("${version.name} ${version.track} (${version.version})", url, imagePath)
            } catch (e: IOException) {
                System.err.println("Failed to download ${version.version}")
                e.printStackTrace(System.err)
            }
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
private fun readVersions(): List<Version> {
    val resource = object {}.javaClass.getResourceAsStream("/versions.json")
    requireNotNull(resource) { "missing versions.json" }
    return resource.buffered().use {
        Json.Default.decodeFromStream(it)
    }
}

private fun download(name: String, url: URL, to: Path) {
    val connection = url.openConnection()
    val contentLength = connection.contentLengthLong
    val progressStream = ProgressBar.wrap(
        connection.getInputStream().buffered(),
        ProgressBarBuilder()
            .setTaskName(name)
            .setInitialMax(contentLength)
            .setUnit(" mb", 1024)
            .setConsumer(ConsoleProgressBarConsumer(System.out))
    ) as ProgressBarWrappedInputStream
    TarArchiveInputStream(GzipCompressorInputStream(progressStream)).use { tar ->
        for (entry in tar) {
            if (entry.name == "android-studio/lib/resources.jar") {
                val zip = ZipArchiveInputStream(tar)
                for (resource in zip) {
                    if (resource.name == "artwork/studio_splash@2x.png") {
                        to.outputStream().buffered().use { out ->
                            zip.copyTo(out)
                        }
                        // just jump to end
                        progressStream.progressBar.apply { stepTo(max) }
                        break
                    }
                }
                break
            }
        }
    }
}

@Serializable
class Version(
    val name: String,
    val track: Track,
    val version: String,
)

@Serializable
enum class Track {
    Stable, Beta, Canary
}

private operator fun TarArchiveInputStream.iterator(): Iterator<ArchiveEntry> = iterator {
    while (nextEntry != null) {
        yield(currentEntry)
    }
}

private operator fun ZipArchiveInputStream.iterator(): Iterator<ZipEntry> = iterator {
    while (true) {
        val entry = nextEntry ?: break
        yield(entry)
    }
}