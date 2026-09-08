# NGI PRO build and validation record

**Run date:** 2026-09-08 (Asia/Karachi)

This file records what was actually executed. It intentionally does not mark an APK as built or runtime-ready without an Android toolchain and device result.

## Repository reconciliation

- Working branch: `arena/01a07f11-mylocalproject`
- Fetched remote refs before reconstruction:
  - arena branch: `9e870fb8ba8dc0f4ba4f0dff596da48785b8cb22`
  - feature branch: `b658b784e549056c9f9d55290f4b4266f7dfff54`
  - `main`: `7bbba52dd7ad8a5e5df3a547391761a8370902df`
- The fetched arena history was merged into this arena branch. No PR was opened and no change was pushed to `main`.

## Reconstructed build inputs

- Root Gradle settings and plugin configuration: `settings.gradle`, `build.gradle`, `gradle.properties`.
- Android module configuration: `decompiled/app/build.gradle`.
- Gradle wrapper: `gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.jar`, and `gradle/wrapper/gradle-wrapper.properties` for Gradle 8.0.2.
- Module identity from the decoded manifest and APK metadata: package/application ID `nika.ngipro`, version `2.0`, versionCode `2`, minSdk `21`, target/compile SDK `33`.
- The APK records Android Gradle Plugin `8.0.0` in `META-INF/com/android/build/gradle/app-metadata.properties`.
- The module compiles the recovered merged dependency source tree under `decompiled/dependencies/src/main/java` rather than adding unverified Maven coordinates. The duplicate decompiler-generated application `R.java` is excluded because AAPT2 generates the application R class from the recovered resources.
- All 25 native-library entries from `NGI_PRO_2.0_decompiled_full.zip` were copied byte-for-byte into `decompiled/app/src/main/jniLibs/` for packaging. The zero-byte arm64 `libNeoLibDumper.so` entry is intentionally preserved as evidence of the source APK; it is not represented as a working dumper.

## Commands executed and results

| Command/check | Result | Evidence |
| --- | --- | --- |
| `git diff --check` | Passed after the reconstruction changes | No whitespace errors reported |
| `python3 -m compileall -q retoolkit/retoolkit` | Passed | All toolkit Python modules compiled |
| `python -m retoolkit.feature_records validate retoolkit/test_project` | Passed | `{"valid": true, "count": 0}` |
| Fresh `python -m retoolkit.pipeline NGI PRO_2.0.apk <temporary-project> arm64-v8a` | Passed | Completed all six stages; temporary output was outside the repository |
| Analyst-supplied command scan with patterns `function` and `analysis` | Passed | 10 candidate records; every worksheet retains unresolved handler/state/vtable fields and manual-verification status |
| Vtable scan in the fresh pipeline | Passed | 205 libc++ candidates, 1 `libngi_pro_engine.so` candidate, 1 `libsdkgen.so` candidate; other libraries produced 0 candidates |
| `./gradlew --version` | **Blocked** | Exact failure: `ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.` |
| `./gradlew clean` / `assembleDebug` / `assembleRelease` | **Not run** | The wrapper cannot start without Java |
| `adb devices` / install / Logcat | **Blocked** | `adb` is not installed and no authorized device/emulator is present |
| `apksigner`, `zipalign`, Android SDK `aapt` | **Blocked** | These executables are not installed |

The fresh toolkit run parsed the original APK successfully despite four non-fatal androguard warnings (`invalid decoded string length` while decoding the recovered manifest/resource data). It reported:

- APK size: 13,862,706 bytes; SHA-256 `7745e4ee4b41e6025a88649f3d409d06087b8f5a11f967f09012f760f8540d73`.
- Seven DEX files and 10,991 total classes.
- Four ABIs and 25 native-library entries.
- Seven arm64-v8a libraries deeply analyzed.
- Metadata, manifest/permission, DEX, native inventory, functions, strings, imports/exports, callgraphs, vtable candidates, and summary reports generated in the temporary validation project.

## Evidence-based status matrix

### Android app

- **Source/resource reconstruction:** Implemented. The recovered manifest, resources, Java sources, generated binding sources, and native libraries are wired into the Gradle module.
- **Debug APK:** **Not built.** Blocked by missing Java, Android SDK platform 33, and build-tools in this environment.
- **Release APK:** **Not built.** Same toolchain blocker. No release keystore is present; no credentials were invented.
- **Release signing:** **Pending.** Even after an unsigned release build becomes possible, an authorized secure keystore must be supplied outside the repository.
- **Device install/UI smoke tests:** **Not run.** No ADB or authorized emulator/device.
- **Logcat/crash verification:** **Not run.** No Android runtime is available.

### Retoolkit P0–P3 capability assessment

The repository does not contain a product specification that assigns a different label to each individual command, so the statuses below are capability-based rather than invented feature claims:

- **P0 — APK/manifest/DEX/native inventory:** **Implemented and executed.** Fresh pipeline output verified package metadata, signing flags, manifest permissions/components, all seven DEX files, class counts, all four ABIs, and all 25 native entries.
- **P1 — Static native analysis:** **Implemented and executed.** Fresh output includes symbols/functions, printable strings, imports/exports, PLT/GOT data, per-library callgraphs, and indirect-call records for arm64-v8a.
- **P2 — Structural exploration:** **Implemented and executed.** Callgraph query support, project-wide search, relocation-aware vtable/function-pointer candidate scanning, and the fresh candidate counts above are present. Candidates are heuristics and require manual confirmation.
- **P3 — Analyst workflow/bridge:** **Implemented and smoke-tested.** Versioned feature-record creation/validation, analyst-supplied command-string discovery, nearby branch/call reporting, unresolved worksheet fields, project layout, and zip/file bridge code are present. Static command output is candidate evidence only; no handler, global, vtable meaning, game-state semantics, hooks, patches, or automation are inferred.

The original reverse-engineering artifacts and retoolkit fixture remain preserved. Runtime claims for the Android UI and native JNI boundary remain **unverified** until the missing toolchain and an authorized test target are supplied.
