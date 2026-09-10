# Pre-Install Audit Report

## 1. Branch audited

- Branch: `arena/01a07f11-mylocalproject`
- Code state audited: `730d808` (`Preserve compatibility jar audit artifact`)
- Documentation commit: follows this audit.
- The Windows device was not used during this audit. The repository's previous Windows result at `2097f80` is recorded separately from the post-audit configuration.

## 2. Startup chain

The verified startup path from the current manifest and recovered source is:

1. Android starts the `nika.ngipro` process and creates the default `android.app.Application`. No custom `Application` class is declared.
2. The manifest's `android:appComponentFactory` resolves `androidx.core.app.CoreComponentFactory`. The post-audit Gradle configuration packages the complete recovered Core runtime instead of relying on a compile-only Core jar.
3. Android creates the non-exported `androidx.startup.InitializationProvider` with the `${applicationId}.androidx-startup` authority.
4. AndroidX Startup reads the two manifest metadata entries:
   - `androidx.emoji2.text.EmojiCompatInitializer`
   - `androidx.lifecycle.ProcessLifecycleInitializer`
5. `InitializationProvider` invokes the recovered Startup implementation. Emoji2 is now an explicit embedded runtime input; lifecycle-process, lifecycle-runtime, lifecycle-common, `Lifecycle`, and `DefaultLifecycleObserver` are included in the extracted runtime closure.
6. Android resolves the launcher `nika.ngipro.LoginActivity`, whose static initializer executes `System.loadLibrary("NGI")`.
7. `LoginActivity.attachBaseContext()` invokes `LocaleHelper.attach()` and then the AppCompat superclass chain.
8. AppCompat creates its delegate and validates the recovered `abc_vector_test` resource. `vectorDrawables.useSupportLibrary = true` is enabled.
9. `LoginActivity.onCreate()` calls `CrashGuard.initNative()`, reads the native configuration values through `libNGI.so`, inflates `login_activity`, creates the WebView, and prepares the OAuth callback flow.
10. If a stored token exists, `LoginActivity` starts `MainActivity`. Loading `MainActivity` then loads `libngi_pro_engine.so` and `libsdkgen.so` and enters its native/feature path.

The prior device crash occurred at step 8, before `LoginActivity.onCreate()`. That VectorDrawable configuration fix has not yet been verified by a post-audit device run.

## 3. Problems found

### 🔴 High — incomplete AndroidX runtime closure

- Location: `decompiled/app/build.gradle`
- Root cause: multiple AndroidX jars, including Core, Activity, Fragment, and lifecycle artifacts, were on a compile-only path or excluded from the runtime file tree. The one-class `ResourcesCompat$ThemeCompat` compatibility approach did not represent the Core runtime.
- Fix: commit `2166f5b` packages the extracted AndroidX runtime closure as one implementation classpath, copies both AAR `classes.jar` files and direct jar artifacts, and adds the recovered versions of Core `1.9.0`, Activity `1.6.0`, Fragment `1.3.6`, Emoji2 `1.2.0`, and lifecycle-process `2.5.1` to the embedded resolution set.
- Verification: `verifyRecoveredRuntimeClosure` now checks required startup/superclass classes and duplicate classes before the build proceeds. A post-change Windows build is still required.

### 🔴 High — AppCompat vector setup crash

- Observed runtime log: `runtime-20260910-164629.log`
- Error: `IllegalStateException: This app has been built with an incorrect configuration. Please configure your build for VectorDrawableCompat.`
- Root cause: AppCompat's `ResourceManagerInternal.checkVectorDrawableSetup()` could not validate `abc_vector_test` during delegate/window setup.
- Fix: commit `4345adb` enables `vectorDrawables.useSupportLibrary = true`; the recovered `abc_vector_test.xml` remains present and its public resource ID is `0x7f070077`.
- Verification: configuration and resource source are present; physical-device verification is pending.

### 🟠 High — generated AndroidX R classes were absent

- Root cause: extracted implementation jars did not carry the generated AndroidX resource classes required by AppCompat initialization.
- Fix: recovered `R.java` files are copied into a generated source directory using the recovered `public.xml` IDs. The former synthetic attribute declarations were removed to avoid resource-merger collisions.
- Verification: the earlier `androidx.appcompat.R$drawable` failure did not recur; the app reached the later VectorDrawable check.

### 🟠 High — malformed dependency implementation sources entered javac

- Root cause: the broad `com/**/*.java` source include compiled malformed decompiler output such as `com/google/common/cache/LocalCache.java`.
- Fix: commits `52cd1ba` and `88dcad1` restrict the source roots to app code, Startup, ViewBinding, and generated recovered R sources. The dependency implementation tree is not a Java source root.
- Verification: the Windows build progressed beyond Java compilation after this change.

### 🟠 High — synthetic resource attributes caused Windows resource-merger failure

- Root cause: adding missing AndroidX-style attributes directly to the app `attrs.xml` caused the Windows AAPT2 merge failure with an invalid virtual path such as `...mergeDebugResources-8:/values/values.xml`.
- Fix: commit `2097f80` removes those synthetic declarations and rewrites recovered `R.java` styleable references to the exact IDs already present in recovered `public.xml`.
- Verification: the Windows build passed resource merging and proceeded through packaging at the pre-closure configuration.

### 🟠 Medium — duplicate LibDumper dispatch

- Location: `MainActivity.runTool()` tool ID 13.
- Root cause: tool ID 13 called `runLibDumperTool()` twice.
- Fix: commit `381f9b7` leaves one call and returns immediately.
- Verification: source contains one tool-13 branch.

### 🟠 High — missing native artifact for `NativeEngine`

- Location: `com/ngi_pro/core/NativeEngine.java`.
- Root cause: the class executes `System.loadLibrary("UENGINE")`, but no `libUENGINE.so` exists in any ABI directory. No caller was found in the recovered app source.
- Fix: no fabricated library or unrelated native substitution was made.
- Verification: this remains a feature-level blocker for any path that loads `NativeEngine`; it is not on the confirmed LoginActivity startup path.

### 🟠 High — JNI symbol mismatch for `listFunctions`

- Locations: `MainActivity`, `IDAViewActivity`, and `libsdkgen.so`.
- Root cause: the native library exports `Java_nika_ngipro_MainActivity_generateSdk`, but the two `listFunctions` symbols are C++-mangled (`_Z...Java_nika_ngipro_...`) rather than exported under the exact JNI names. No `JNI_OnLoad` registration entry was found in the inspected ABI.
- Fix: no binary patch or fake native symbol was created.
- Verification: `MainActivity.listFunctions()` is not called by the current MainActivity tool dispatcher; `IDAViewActivity.listFunctions()` is called by its feature path and remains a native feature risk.

### 🟡 Medium — optional LibDumper native artifact is zero bytes

- Location: `jniLibs/arm64-v8a/libNeoLibDumper.so`.
- Root cause: the recovered file is a zero-byte placeholder; no buildable native source or valid replacement artifact was found.
- Existing handling: `NativeLibWrapper` catches `UnsatisfiedLinkError` and reports native unavailable.
- Verification: startup should not fail on this optional library, but LibDumper functionality is not available on the affected ABI. No fake `.so` was created.

### 🟢 Verified — lifecycle failure from the earlier run

- The earlier `androidx.lifecycle.DefaultLifecycleObserver` failure was fixed by supplying the lifecycle runtime closure. The next confirmed runtime failure was AppCompat VectorDrawable setup, not lifecycle.

## 4. AndroidX dependency closure

The post-audit configuration has one extracted implementation classpath for the embedded AndroidX/Material closure. AAR `classes.jar` files and direct jar artifacts are copied into `build/generated/embedded-libraries`; the same directory is compile-only and implementation-visible, so required classes are not compile-only in the APK.

| Area | Version/source | Runtime supply | Status |
|---|---|---|---|
| AppCompat | `androidx.appcompat:appcompat:1.6.1` | Extracted AAR classes | 🟢 |
| AppCompat resources | transitive from AppCompat | Extracted AAR classes | 🟢 |
| Core | `androidx.core:core:1.9.0` | Extracted runtime jar; full closure, not ThemeCompat-only | 🟢 by configuration; APK pending |
| Activity | `androidx.activity:activity:1.6.0` | Extracted AAR classes | 🟢 by configuration |
| Fragment | `androidx.fragment:fragment:1.3.6` | Extracted AAR classes | 🟢 by configuration |
| Lifecycle | runtime/common/process `2.5.1` | Extracted runtime jars/AARs | 🟢 by configuration |
| Startup | recovered local source | `androidx/startup` source root; startup-runtime jar skipped | 🟢 |
| Emoji2 | `androidx.emoji2:emoji2:1.2.0` | Extracted runtime AAR | 🟢 by configuration |
| SavedState/customview/collection/annotation | resolved transitives | Extracted runtime artifacts | 🟢 by configuration |
| VectorDrawable/transition/versionedparcelable | resolved transitives | Extracted runtime artifacts | 🟢 by configuration |
| ConstraintLayout | `2.1.4` plus resolved core | Extracted runtime artifacts | 🟢 by configuration |
| RecyclerView/DrawerLayout/CardView/Material | recovered coordinates in build file | Extracted runtime AARs | 🟢 by configuration |
| ViewBinding | recovered local source | ViewBinding source root | 🟢 |

`ResourcesCompat$ThemeCompat.java` remains in the recovered tree for provenance but is excluded from the main source set because the complete Core runtime now supplies the nested class. The historical filtered compatibility JAR is retained as a validation artifact only and is not used as the APK's Core replacement.

## 5. Native library matrix

All non-placeholder files were inspected with ELF tools. System dependencies such as `libc.so`, `libm.so`, `libdl.so`, `liblog.so`, and `libandroid.so` are platform libraries; `libc++_shared.so` is packaged per ABI.

| ABI | Libraries and size in bytes | ELF |
|---|---|---|
| arm64-v8a | `libNGI.so` 6312; `libcrash.so` 6376; `libextractor.so` 5632; `libngi_pro_engine.so` 121576; `libsdkgen.so` 111648; `libc++_shared.so` 1022136; `libNeoLibDumper.so` 0 | ELF64 AArch64; NeoLibDumper is not an ELF |
| armeabi-v7a | `libNGI.so` 4672; `libcrash.so` 4564; `libextractor.so` 4240; `libngi_pro_engine.so` 93420; `libsdkgen.so` 84396; `libc++_shared.so` 609992 | ELF32 ARM |
| x86 | `libNGI.so` 4848; `libcrash.so` 4780; `libextractor.so` 4528; `libngi_pro_engine.so` 135420; `libsdkgen.so` 112816; `libc++_shared.so` 990124 | ELF32 Intel 80386 |
| x86_64 | `libNGI.so` 6376; `libcrash.so` 6408; `libextractor.so` 5776; `libngi_pro_engine.so` 139072; `libsdkgen.so` 122376; `libc++_shared.so` 1046192 | ELF64 x86-64 |

All required startup libraries for the tested arm64 path are non-zero. `libNeoLibDumper.so` is the explicit exception.

## 6. Resource audit

- Recovered `public.xml` IDs are retained.
- Missing recovered styleable references such as `fontWeight`, `ttcIndex`, `fontStyle`, and related font-provider attributes are mapped to existing public IDs during R-source preparation instead of being declared as synthetic app resources.
- `abc_vector_test.xml` exists under `res/drawable` and is listed in `public.xml` as `0x7f070077`.
- The prior Windows invalid-path resource merge was caused by synthetic attribute declarations and was removed.
- App Java resource references were statically checked: all app layout references resolve to recovered layouts, and all checked app IDs exist in recovered XML resources.
- Final APK resource-table inspection after the post-audit build is still pending.

## 7. Activity/layout audit

- 20 recovered AppCompat activity classes were found in source.
- The 12 activities declared in the manifest all have matching source classes.
- Login layout: `login_activity` exists.
- Other statically checked AppCompat layouts include `activity_bookmarks`, `activity_dex_view`, `activity_hex_editor`, `activity_ida_view`, `activity_profile`, and `activity_settings`.
- Recovered binding classes are compiled from the ViewBinding source root; generated ViewBinding is disabled to avoid duplicate classes.
- The eight additional recovered activities not declared in the manifest are not on the confirmed launcher chain. Their source references should be reviewed before exposing them as new manifest entry points; they were not silently added.
- Material widgets, RecyclerView, CardView, DrawerLayout, and Toolbar classes are supplied by the extracted runtime closure.

## 8. JNI audit

| Java declaration | Library | Static result |
|---|---|---|
| `LoginActivity.getConfigValue` | `libNGI.so` | Exact `Java_nika_ngipro_LoginActivity_getConfigValue` export found |
| `CrashGuard.initNative`, `executeProtected` | `libcrash.so` | Exact exports found |
| `Extractor.getApkPath`, `readManifest` | `libextractor.so` | Exact exports found |
| MainActivity analysis/patch/search methods | `libngi_pro_engine.so` | Exact exports found except `listFunctions` |
| MainActivity `generateSdk` | `libsdkgen.so` | Exact export found |
| IDAViewActivity engine methods | `libngi_pro_engine.so` | Exact exports found except `listFunctions` |
| IDAViewActivity `listFunctions` | `libsdkgen.so` | Exact JNI export absent; only mangled symbol found |
| `NativeEngine.startAutoSDKGen` | `libUENGINE.so` | Library and symbol absent |
| NativeLibWrapper methods | `libNeoLibDumper.so` | Cannot verify; library is zero bytes and optional load is caught |

## 9. Manifest audit

- Launcher is `nika.ngipro.LoginActivity`, exported with MAIN/LAUNCHER.
- OAuth callback is `ngi://callback` and remains present.
- Startup provider is non-exported and uses `${applicationId}.androidx-startup`.
- Debug application ID is `nika.ngipro.debug` through the Gradle suffix; the manifest placeholders are resolved by Gradle.
- Source manifest metadata says version 2/code 2 while Gradle declares version 2.1/code 3. Gradle's `defaultConfig` is the build authority, matching the previously produced debug APK. This is a recovered-manifest inconsistency, not a launcher dependency.
- `usesCleartextTraffic=true`, Internet, network state, and storage permissions are retained from recovered behavior.
- Optional AndroidX window extension libraries remain optional.

## 10. Remaining risks

1. The post-audit runtime closure has not yet been built and installed on Windows. The previous successful build was before the full Core/Activity/Fragment/Emoji2/lifecycle closure change.
2. The latest VectorDrawable fix has not yet been device-verified.
3. `libUENGINE.so` is absent, so any code path that initializes `NativeEngine` is blocked.
4. The two `listFunctions` JNI methods do not have exact exports in the recovered `libsdkgen.so`; the IDA function-list feature remains blocked unless the original native artifact/source is recovered.
5. LibDumper is optional but unavailable where `libNeoLibDumper.so` is zero bytes.
6. Final APK class/resource/native table inspection must be performed after the post-audit Windows build.

## 11. Build verification

- Previous Windows verification at `2097f80`: clean build, duplicate-class check, manifest checks, APK packaging, compatibility-artifact check, install, and launcher invocation passed; runtime stopped at the VectorDrawable configuration exception.
- Post-audit source state (`730d808` code state) has not yet been built on Windows because the requested static audit was performed first.
- Sandbox build execution is unavailable because the sandbox has no Java/Android SDK; Windows Gradle 8.5 remains authoritative.

## 12. Final verdict

**NOT READY — BLOCKED BY:**

- post-audit Windows static build/APK inspection is still required;
- post-audit device verification of the VectorDrawable and complete AndroidX closure is still required;
- unresolved recovered native artifacts/symbols block NativeEngine and IDA `listFunctions` feature reliability;
- zero-byte LibDumper remains an optional but nonfunctional feature artifact.

The next device installation must be treated as verification only after the post-audit build and final APK closure checks pass. No unrelated feature was removed, no native library was fabricated, and no manifest Startup component was disabled.
