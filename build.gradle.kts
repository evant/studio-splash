plugins {
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.serialization") version "1.9.21"
    application
    `java-base`
}

group = "me.tatarka.android"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("org.apache.commons:commons-compress:1.25.0")
    implementation("me.tongfei:progressbar:0.10.0")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass = "me.tatarka.android.MainKt"
}