# NGI PRO 2.0 Android source module

This directory contains the cleaned Android source/resources required by the Gradle project at the repository root. It is organized as an Android application module under `app/` plus recovered bundled dependency Java sources under `dependencies/`.

## Application identity

- Package/application ID: `nika.ngipro`
- Version: `2.0`
- Version code: `2`
- Minimum SDK: `21`
- Target/compile SDK: `33`
- Launcher: `nika.ngipro.LoginActivity`
- OAuth callback scheme: `ngi://callback`

## Build layout

- `app/build.gradle` — module configuration.
- `app/src/main/AndroidManifest.xml` — manifest.
- `app/src/main/java/` — application Java source and generated binding source.
- `app/src/main/res/` — decoded Android resources.
- `app/src/main/assets/` — packaged assets.
- `app/src/main/jniLibs/<abi>/` — packaged native libraries for arm64-v8a, armeabi-v7a, x86, and x86_64.
- `dependencies/src/main/java/` — recovered source for bundled AndroidX, Material, Glide, Guava, smali/dexlib2, Kotlin runtime/coroutines, and related classes used by the recovered app source.

The module uses the recovered merged dependency source tree rather than inventing Maven coordinates whose original Gradle metadata was not preserved. AAPT2 generates the application `R` class from the resource tree. Recovered view-binding classes are retained, so view binding generation is disabled to avoid duplicate classes.

## Native payload

The JNI declarations and Java/native wrappers are retained in the app source. The native libraries are prebuilt inputs only; their original C/C++ implementations are not reconstructed here. The zero-byte `libNeoLibDumper.so` placeholder remains represented in the recovered native payload and is not treated as a working dumper.

## App-layer additions

The `nika.ngipro.mod` package provides the local-first Mod Manager, versioned models and persistence, profile management, import/export and backup/restore, diagnostics, and the safe `NativeIntegration` boundary. Native handlers, globals, vtables, state interactions, and compatibility not established by evidence remain unresolved or `Unknown`.

## Verification limitation

This source tree is decompilation output and may require manual cleanup of synthetic/decompiler artifacts. The project configuration is reconstructed, but compilation, APK assembly, signing, installation, Logcat, and UI runtime validation require Java 17, Android SDK/build-tools, and an authorized test device or emulator.
