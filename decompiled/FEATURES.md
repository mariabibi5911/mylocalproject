# NGI PRO 2.1 - New Features

## Recently Added Features

### 1. Export Reports (ReportGeneratorActivity)
Generate comprehensive analysis reports in PDF or HTML format including:
- Analysis summary
- Permissions breakdown
- Components list
- Native libraries info
- Code statistics
- Security warnings

### 2. Plugin System (PluginManagerActivity)
Extend functionality via plugins:
- Install/Manage plugins
- Built-in plugins: Decompiler Plus, String Decryptor, API Analyzer
- Plugin repository with 12+ available plugins
- Enable/disable/update plugins

### 3. Smali Editor (SmaliEditorActivity)
Edit smali code directly:
- Load .smali files
- Edit smali code with syntax hints
- Save modifications
- Support for full smali syntax

### 4. Signature Verification (SignatureVerifierActivity)
Verify APK signatures:
- Select APK to verify
- Support for SHA-1, SHA-256, MD5 algorithms
- Signature validation using jarsigner methodology
- Detailed signature information

### 5. Network Traffic Inspector (Planned)
Monitor app HTTP/HTTPS calls:
- Real-time traffic monitoring
- Request/response inspection
- Header analysis
- SSL/TLS decryption support

### 6. Permission Analyzer (PermissionAnalyzerActivity)
Visual breakdown of app permissions:
- Dangerous permissions (red)
- Normal permissions (yellow)
- Signature permissions (white)
- Risk assessment

### 7. Code Search & Navigation (CodeSearchActivity)
Jump to definitions in disassembled code:
- Search classes, methods, strings
- Find usages
- Navigate between components
- Quick jump to definition

## Original Features (v2.0)
- Login/Authentication system
- Hex Editor (25KB+ implementation)
- DEX Disassembler
- IDA View integration
- App Extractor
- Bookmarks Manager
- Mod Manager with version control
- Profile Management

## Build Configuration
- Fixed resource errors (private Android framework colors)
- Added proper Gradle build files
- Added dependencies for AndroidX and Material Design
- Configured ProGuard rules for new features
