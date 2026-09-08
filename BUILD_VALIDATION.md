# NGI PRO build and signing status

This branch has been cleaned to retain only the Android build inputs and documentation needed for the next build/signing step.

## Retained build inputs

- Gradle root configuration and wrapper.
- Android module configuration under `decompiled/app`.
- Recovered Java sources and bundled dependency sources.
- Decoded manifest and resources.
- Packaged assets and ABI-specific native libraries.
- No keystore, password, private key, or signing credential is stored in the repository.

## Reconstructed build configuration

- Application ID: `nika.ngipro`
- Version: `2.0`, versionCode `2`
- minSdk: `21`
- target/compile SDK: `33`
- Android Gradle Plugin: `8.0.0`
- Gradle wrapper distribution: `8.0.2`
- Release signing: intentionally external and pending an authorized keystore

## Commands for the next step

```bash
./gradlew clean
./gradlew assembleDebug
./gradlew assembleRelease
```

The debug APK must be produced and validated before the release build is accepted. The release output should remain unsigned until an authorized signing configuration is supplied. After signing, verify the result with `apksigner` and install only on an authorized device or emulator.

## Previous environment blocker

At the time of reconstruction, the checkout environment had no Java command, Android SDK/platform 33, Android build-tools, `adb`, `aapt`, `apksigner`, or `zipalign`.

The wrapper initially failed with:

```text
ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
```

A temporary Java 17 runtime allowed the wrapper to start, but Gradle 8.0.2 could not be downloaded because of:

```text
javax.net.ssl.SSLHandshakeException: Remote host terminated the handshake
```

Therefore no debug APK, release APK, signed APK, device install, Logcat capture, or Android runtime test is claimed yet.
