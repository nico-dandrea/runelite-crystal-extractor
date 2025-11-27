# Crystal Extractor Helper

RuneLite plugin that highlights the Crystal extractor after a mote harvest until you interact with it.

## Build
- `.\gradlew.bat clean shadowJar` (jar is created at `build\libs\crystal-extractor-0.1.0-all.jar`)

## Run
- Dev harness (no Jagex login): `java -ea -jar build\libs\crystal-extractor-0.1.0-all.jar`
- Official client (supports Jagex account): copy the jar to `%UserProfile%\.runelite\plugins\`, launch RuneLite via the official launcher, and enable the plugin under External Plugins.
