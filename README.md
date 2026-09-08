# NGI PRO 2.0 Android build workspace

This branch is the cleaned Android project used to reconstruct, build, package, and sign NGI PRO. Reverse-engineering archives, PC-side analysis scripts, generated reports, and the original APK are intentionally not kept in this build-focused branch.

## Build inputs kept in this branch

- `settings.gradle`, `build.gradle`, and `gradle.properties` — root Gradle configuration.
- `gradlew`, `gradlew.bat`, and `gradle/wrapper/` — Gradle 8.0.2 wrapper.
- `decompiled/app/build.gradle` — Android application module configuration.
- `decompiled/app/src/main/AndroidManifest.xml` — application manifest and component declarations.
- `decompiled/app/src/main/java/` — recovered NGI PRO application Java sources, generated binding sources, authentication/session classes, and the local Mod Manager implementation.
- `decompiled/dependencies/src/main/java/` — recovered bundled dependency sources required by the app module.
- `decompiled/app/src/main/res/` — Android resources.
- `decompiled/app/src/main/assets/` — packaged application assets.
- `decompiled/app/src/main/jniLibs/` — native libraries packaged by ABI.
- `BUILD_VALIDATION.md` — build, signing, and environment status.

The Android module keeps the recovered application identity: package `nika.ngipro`, version `2.0`, versionCode `2`, minSdk `21`, and target/compile SDK `33`. The build configuration uses the recovered Android Gradle Plugin version `8.0.0` and compiles the bundled dependency source tree without adding unverified dependencies.

## Build commands

From the repository root, with Java 17, Android SDK platform 33, and Android build-tools installed:

```bash
./gradlew clean
./gradlew assembleDebug
./gradlew assembleRelease
```

Expected outputs:

```text
decompiled/app/build/outputs/apk/debug/app-debug.apk
decompiled/app/build/outputs/apk/release/app-release-unsigned.apk
```

The release variant intentionally has no repository-stored keystore. Signing must use an authorized keystore supplied outside the repository, for example through Gradle properties or a local signing configuration. Do not commit passwords, private keys, or keystore files.

## App-layer additions retained

The recovered app source includes the local-first Mod Manager expansion:

- Versioned mod, feature, profile, status, compatibility, dependency, and conflict models.
- Search, filtering, details, enable/disable state, and profile management.
- Import/export, backup/restore, reset confirmation, and diagnostics export.
- Schema-versioned persistence with migration support.
- A safe replaceable `NativeIntegration` boundary that does not invoke unresolved native handlers or addresses.
- Replaceable authentication/session abstractions that do not collect passwords, extract tokens, bypass authentication, or intercept credentials.

Unresolved handlers, globals, vtables, state interactions, and compatibility remain explicitly unresolved or `Unknown`. No gameplay automation, hooks, patch generation, or generic cheating infrastructure is included.
