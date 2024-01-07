# Studio Splash

An archive of all the Android Studio splash images

# Updating

Versions of Android Studio are fetched from https://developer.android.com/studio/archive
according to `src/main/resources/versions.json`. To add a new version, add an entry with the version name,
the track (Canary, Beta, Stable), and the full version number. Then run

```shell
./gradlew run
```

to download any updates. Splash images tend to only change on major canary and stable builds so only one of each is
needed.

# Licenses

- Code is licensed under Apache Version 2.0
- Images are licensed under Apache Version 2.0

```
Copyright (C) 2020-2023 The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```