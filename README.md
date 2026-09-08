# NGI PRO 2.0 — decompiled workspace

This repository contains the `NGI PRO 2.0` Android APK and a source-oriented decompilation of it. The app is an Android native reverse-engineering toolkit for inspecting ELF/shared-library files and DEX/APK files.

## What is included

- `NGI PRO_2.0.apk` — the original APK supplied in the repository.
- `decompiled/app/src/main/java/` — the app's 44 decompiled Java classes, including the `nika.ngipro` activities/adapters/managers, generated view-binding classes, the `R` class, and the two project-owned native wrappers (`com.neomods` and `com.ngi_pro`).
- `decompiled/app/src/main/res/` — decoded resources extracted from the APK.
- `decompiled/app/src/main/AndroidManifest.xml` — the decoded manifest.
- `decompiled/app/src/main/jniLibs/` — the 25 native-library entries recovered from the full archive, including all four ABIs and the original zero-byte `libNeoLibDumper.so` placeholder.
- `decompiled/dependencies/src/main/java/` — decompiled third-party bundled/library classes kept separately from the app code.
- `NGI_PRO_2.0_decompiled_full.zip` — the complete decompilation artifact already present on `main`, including source, resources, DEX files, and native libraries.
- `NGI_PRO_app_code_only.zip` — the smaller app-only source artifact from `main`.
- `retoolkit.zip` — the PC-side APK/native analysis companion added to the latest `main`.
- `retoolkit/` — the expanded Python toolkit and its generated NGI PRO analysis fixture. It creates `features/` and `offsets/`, resolves relocation-backed vtable candidates, and supports analyst-supplied static command discovery.
- `decompiled/README.md` — the detailed inventory, behavior notes, native boundary, and security findings.

The application classes are Java decompilation output. The APK contains Kotlin runtime/coroutine dependencies, but no original Kotlin source can be recovered byte-for-byte from DEX; the decompiler emits Java for those classes.

## Main behavior

After GitHub OAuth login, the main screen lets a user select a file and run native analysis features such as ELF auto-analysis, symbol/function discovery, ARM64 disassembly, DEX inspection, CFG generation, import/section listing, cross-reference lookup, binary search, byte patching, a hex editor, SDK generation, and bookmarks. Most binary-analysis work is implemented by JNI libraries in the APK rather than by the Java UI layer.

## Reconstructed Android build

The missing Gradle project has now been reconstructed at the repository root:

```text
settings.gradle                 # root project, maps :app to decompiled/app
build.gradle                    # Android Gradle Plugin 8.0.0
gradle.properties
app module                      # decompiled/app/build.gradle
Gradle wrapper                  # ./gradlew and ./gradlew.bat, Gradle 8.0.2 distribution
```

The configuration is evidence-based: the original APK records AGP `8.0.0`, compile/target SDK 33, min SDK 21, and AndroidX/Material versions in `META-INF`. The recovered merged dependency Java sources are compiled as part of the app module instead of adding unverified Maven coordinates. The decoded app resources generate the application `R` class; the decompiler's duplicate `nika.ngipro.R.java` is excluded. Recovered native libraries are supplied through `jniLibs` without changing their contents.

From a machine with Java 17, Android SDK platform 33, and Android build-tools installed:

```bash
./gradlew clean
./gradlew assembleDebug
./gradlew assembleRelease
```

The release build is deliberately not assigned a keystore. A secure authorized signing configuration must be supplied outside the repository before signing or distribution; an unsigned release artifact, if produced, is not a signed release. See [`BUILD_VALIDATION.md`](BUILD_VALIDATION.md) for the exact validation performed in this checkout and the current blockers.

## Where to make the next changes

Start with `decompiled/app/src/main/java/nika/ngipro/`. The app-facing JNI declarations are in `MainActivity`, `IDAViewActivity`, `LoginActivity`, `Extractor`, and `CrashGuard`. The native implementations are still compiled `.so` files inside the original APK/full archive, so adding or changing native behavior will require a separate NDK reconstruction or replacement.

This remains a faithful reverse-engineering workspace: decompiled code can contain compiler/decompiler artifacts and the original Gradle dependency metadata was not stored in the APK. The build files are a reproducible reconstruction, but an APK is not claimed until the Android toolchain compiles it and runtime checks pass.

## NGI PRO mod manager expansion

The decompiled app now includes a local-first mod-management layer under `decompiled/app/src/main/java/nika/ngipro/mod/`:

- `FeatureRegistry` preserves the existing research identifiers (`ESP::LINES`, `ESP::ENEMY_LINES`, `ESP::POCKETS`, `ESP::STATES`, `AUTO::PLAY`, and `AUTO::QUEUE`) as registry records. Native handlers, globals, vtables, and state interactions are retained as explicit `unknown` / `manual-review` metadata; no target-specific behavior is inferred.
- `ModDefinition`, `ModFeature`, and `ModProfile` provide versioned models for descriptions, authors, categories, compatibility, dependencies, conflicts, status, configuration, and presets.
- `ModStore` persists schema-versioned JSON atomically in app-private storage, migrates older state, supports backup/restore, and keeps import/export separate from the original APK/native research artifacts.
- `ModManagerActivity`, `ModDetailActivity`, and `DiagnosticsActivity` provide small-screen-friendly search, filters, enable/disable controls, profile creation/rename/duplicate/delete/activation, import/export, backup/restore/reset confirmations, feature detail, status/compatibility indicators, and diagnostics export.
- `NativeIntegration` is the only native-facing boundary. The shipped `NoOp` adapter saves configuration locally without invoking unresolved addresses or handlers. A verified authorized adapter can be supplied later without putting native logic in the UI.
- `AuthenticationService` and `SessionStore` provide a replaceable account/session boundary and store only provider, username, avatar URL, and timestamp. The existing GitHub OAuth WebView flow remains the provider implementation; no password collection, token extraction, bypass, or credential replacement was added.

The main tool grid exposes **Mod Manager** without requiring an ELF target file. Existing analysis activities, JNI declarations, address notes, XREFs, native-module information, and the original research/retoolkit artifacts remain unchanged and outside the new UI's hard-coded behavior.

### Validation note

Source/resource configuration, the recovered native payload, and the PC toolkit were validated in this checkout. The toolkit completed a fresh end-to-end analysis of the original APK for all four ABIs and deep analysis for arm64-v8a; it found seven DEX files, 10,991 classes, seven native libraries, and produced functions, strings, imports/exports, callgraphs, and vtable candidates. Java compilation, APK assembly, device installation, Logcat, and UI smoke tests remain blocked in this environment because Java, Android SDK/build-tools, and ADB are not installed. The original `NGI PRO_2.0.apk` is not overwritten.
