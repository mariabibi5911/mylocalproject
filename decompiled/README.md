# NGI PRO 2.0 decompilation notes

## Provenance and verification

The APK and the two ZIP artifacts were compared against the `main` branch. The APK on this working branch is byte-for-byte identical to `main`:

```text
SHA-256: 7745e4ee4b41e6025a88649f3d409d06087b8f5a11f967f09012f760f8540d73
```

`NGI_PRO_2.0_decompiled_full.zip` was expanded into an edit-friendly layout. The expanded tree contains 5,042 Java source files: 44 app/project classes under `app/src/main/java` (42 `nika.ngipro` classes plus the `com.neomods` and `com.ngi_pro` native wrappers) and 4,998 bundled/dependency classes under `dependencies/src/main/java`. It also contains 1,058 decoded resource files, the decoded manifest, and the APK asset `LIB-DUMP.sh`.

The ZIP is retained as the complete artifact from `main`; it also preserves the original extracted DEX files and native libraries that are intentionally not duplicated in the readable source tree.

The latest `main` branch also contains `retoolkit.zip`. It is a separate PC-side Python companion that extracts APK metadata, permissions, all DEX files, every ABI's native libraries, symbols, strings, imports/exports, call graphs, and vtable candidates. Its expanded source and the generated NGI PRO fixture are under `retoolkit/` at the repository root. The fixture confirms seven DEX files with 10,991 total classes and 25 native-library entries across four ABIs; the zero-byte `libNeoLibDumper.so` is reported as invalid ELF. The toolkit now also preserves analyst-owned `features/` and `offsets/` folders, resolves relocation-backed function-pointer candidates, and supports generic analyst-supplied command-string discovery without assuming a handler or state semantics.

## Application identity

| Item | Value |
| --- | --- |
| Package | `nika.ngipro` |
| Version | `2.0` (`versionCode` 2) |
| Minimum SDK | 21 |
| Target/compile SDK | 33 |
| Launcher | `nika.ngipro.LoginActivity` |
| File format focus | ELF/shared objects and DEX/APK files |

## Reconstructed feature map

### Authentication and profile

- `LoginActivity` displays an in-app GitHub OAuth WebView.
- The callback is `ngi://callback` and the OAuth code is exchanged for a GitHub access token.
- User profile data is requested from `https://api.github.com/user`.
- Login/profile fields are stored in the `NGI_PREFS` preferences file.
- `XorUtil` applies a repeating XOR key followed by Base64. This is obfuscation, not encryption.
- `ProfileActivity`, `SettingsActivity`, `LocaleHelper`, and `ThemeManager` handle profile, language, accent color, and preferences.

### Binary and DEX tooling

`MainActivity` exposes the following tools from its grid:

1. Auto analysis
2. Function discovery
3. Strings/smart scanning
4. ARM64 disassembly
5. DEX disassembly
6. Function decompilation/pseudocode
7. Hex patcher
8. Binary search
9. ELF section listing
10. Import resolution
11. Control-flow graph generation
12. SDK generation
13. Library dumper (the shipped APK reports `libNeoLibDumper.so` missing because its arm64 entry is zero bytes)
14. Hex editor
15. IDA-style function view
16. Cross-references/callers
17. Function explorer
18. Bookmarks

`HexEditorActivity` supports bounded in-memory editing (up to 20 MiB), search, goto-offset, one-level undo, and saving. `IDAViewActivity` combines symbol-derived functions with native code scanning and can show ARM64 disassembly, pseudocode, and CFG/reference information. `DexDisassemblerActivity` handles DEX entries. The app also contains package/APK extraction and manifest-preview helper classes.

`SimpleDecompiler` is explicitly a beta placeholder: it writes a small `printf("soon working!")` sample rather than decompiling a native function. The production-looking decompiler UI delegates to the native engine instead.

### Native boundary

The Java layer loads these libraries:

- `libngi_pro_engine.so` — ELF analysis, scanning, disassembly, CFG, pseudocode, patching, search, references, and dump operations.
- `libsdkgen.so` — symbol extraction and SDK generation.
- `libNGI.so` — configuration lookup used by the login flow.
- `libcrash.so` — protected execution/crash guard.
- `libextractor.so` — APK path and manifest helpers.
- `libNeoLibDumper.so` — present as a zero-byte arm64 entry in the APK; the Java UI references the bundled `com.neomods.libdumper.jni.NativeLibWrapper`, but its loader reports the library as unavailable and the UI tells the user to build/place it under `app/src/main/jniLibs/<abi>/`.

The archive also contains `com.ngi_pro.core.NativeEngine`, which loads `libUENGINE.so`. No `libUENGINE.so` is packaged and the class has no app call site in the recovered Java, so it appears to be an unused/orphaned engine hook.

The readable Java sources retain the JNI method declarations and call sites. The C/C++/Rust implementations cannot be converted into Java/Kotlin source by a DEX decompiler; their original binary payloads remain available in the APK and complete ZIP artifact.

## Manifest and security observations

These notes preserve the shipped behavior; they are not silently changed because doing so would make the source diverge from the APK.

- The manifest requests `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE`, and `MANAGE_EXTERNAL_STORAGE`, which are broad file-access permissions.
- `android:debuggable="true"`, backup is enabled, and `usesCleartextTraffic="true"` is set in the shipped manifest. These should be reviewed before a production release.
- `libNGI.so` contains configuration-key labels for `CLIENT_ID`, `CLIENT_SECRET`, and `REDIRECT_URI`. Any OAuth client secret packaged in an APK should be treated as public and rotated/replaced with a server-side exchange.
- A GitHub access token is held in app preferences using XOR/Base64 obfuscation. Anyone with the app data or APK can reproduce this scheme; it should not be considered secure storage.
- The update checker contacts the raw GitHub URL embedded in `UpdateChecker` and opens the returned link, so update metadata and release provenance should be authenticated before trusting it.
- The OAuth WebView enables JavaScript and DOM storage. It should be limited to the expected origin/callback and reviewed for token leakage before new features are added.

## Working on the next feature

1. Modify only app code under `decompiled/app/src/main/java/nika/ngipro/` first.
2. Keep third-party/dependency output under `decompiled/dependencies/` as reference material.
3. If the change calls a new native function, add a Java declaration and corresponding JNI implementation/library; a Java-only change cannot create a missing native symbol.
4. Reconstruct a Gradle/Android Studio build and run on a test device before producing a replacement APK.
5. For security-sensitive work, replace the embedded OAuth-secret/token flow and narrow the manifest permissions as a deliberate behavior change.

Decompilation output is readable and useful for analysis, but it is not guaranteed to compile without manual cleanup of synthetic lambda names, generated binding details, and missing original build configuration.
