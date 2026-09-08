# NGI PRO security and data-flow audit

**Scope:** Static audit only. No application source, manifest, resources, native libraries, or build configuration were modified for this audit. No credentials, tokens, cookies, or discovered servers were contacted. The audit covered the retained Java/resources/manifest/native libraries and the original APK/DEX recovered temporarily from prior Git history for inspection.

## A. External hosts

| Host / destination | Official/custom | Purpose | Data sent | Evidence | Confidence |
| --- | --- | --- | --- | --- | --- |
| `github.com/login/oauth/authorize` | Official GitHub | Starts GitHub OAuth authorization in an in-app WebView | `client_id`, `scope=user`, configured callback URI; the WebView itself handles the GitHub login page | `LoginActivity.java:171-172` | High |
| `github.com/login/oauth/access_token` | Official GitHub | Exchanges the OAuth authorization result for an access token | POST body contains configured client ID, embedded client secret, and authorization code | `LoginActivity.java:224-238` | High |
| `api.github.com/user` | Official GitHub API | Fetches the authenticated user profile | `Authorization: token <runtime OAuth token>` header | `LoginActivity.java:267-278` | High |
| `raw.githubusercontent.com/Sanji1-Owner/NGIPRO/main/update.json` | Official GitHub host; developer-controlled repository content | Periodic/manual update metadata check | Fixed `User-Agent: NGI-PRO-App`; response is expected to contain version, changelog, and a dynamic update URL | `UpdateChecker.java:32, 80-101` | High |
| Dynamic `avatar_url` from `api.github.com/user` | Dynamic; normally GitHub avatar/CDN but not allowlisted | Loads the profile avatar through Glide | No app account fields are added; the URL is fetched by Glide's `HttpURLConnection` loader | `LoginActivity.java:274-278`, `MainActivity.java:211-221`, `ProfileActivity.java:40-53` | High for the data flow; host depends on API response |
| Dynamic `lib_url` from update JSON | Unknown/developer-controlled | Opened only after the user selects “Update Now” | No app data is appended by the app; an `ACTION_VIEW` intent is sent to an external app/browser | `UpdateChecker.java:91-101, 131-136` | High for dynamic behavior; actual host unknown without fetching the file |

### Non-runtime URL literals

- `http://schemas.android.com/...`, `http://schemas.android.com/apk/res-auto`, and `http://ns.adobe.com/xap/1.0/` are XML/resource namespace or metadata literals, not application network destinations.
- `https://android.googlesource.com/toolchain/llvm-project` appears in native compiler identification strings, not as an app request.
- `https://services.gradle.org/distributions/gradle-8.0.2-bin.zip` is build-tooling configuration, not an app runtime endpoint.
- Gradle `google()` and `mavenCentral()` repositories are build-time dependency sources, not runtime application destinations.

No hard-coded IP address, WebSocket endpoint, Telegram endpoint, Discord webhook, Firebase endpoint, or generic webhook was found in the app-owned source, resources, DEX strings, or native-library strings.

## B. Authentication flow

Observed flow:

```text
User taps the GitHub login card
→ LoginActivity creates an in-app WebView
→ WebView loads github.com/login/oauth/authorize
→ GitHub renders the account authorization page inside the WebView
→ GitHub redirects to ngi://callback with an authorization code
→ LoginActivity intercepts the callback and extracts only the code parameter
→ LoginActivity sends a POST to github.com/login/oauth/access_token
→ GitHub returns JSON containing an OAuth access token
→ LoginActivity stores the token locally using reversible XOR/Base64 obfuscation
→ LoginActivity sends the token to api.github.com/user in an Authorization header
→ LoginActivity stores returned username and avatar URL locally
→ MainActivity/ProfileActivity may load the avatar URL through Glide
```

Important implementation details:

- Authentication uses an **in-app WebView**, not an external browser or custom tab.
- JavaScript and DOM storage are enabled for that WebView.
- The callback is registered in the manifest as `ngi://callback`.
- Callback detection uses a configured URI prefix check. Non-callback URLs are allowed to continue loading in the WebView.
- The token exchange is performed directly by the application using `HttpURLConnection` against GitHub.
- The exchange includes the client secret returned by the native `libNGI` configuration bridge. The actual client secret was not printed.
- The token is checked on later startup for presence, but no additional non-GitHub token request was found in the app-owned source.

## C. Credential handling

`PASSWORD: not observed` in app-owned credential collection or request-building code.

`OAUTH TOKEN: observed` in the OAuth response parser, local persistence path, and GitHub API authorization header. The actual value was not printed.

| Question | Result | Evidence / qualification |
| --- | --- | --- |
| Password collected by the app | **NO** | No app-owned password field, password variable, or password POST was found. The GitHub page is displayed in a WebView and may handle its own login form internally. |
| Password transmitted by app-owned code | **NO** | No application request serializes a password. The WebView may transmit credentials to GitHub as part of the remote GitHub page, but that page flow is not application-owned or inspected by this source audit. |
| OAuth token stored locally | **YES** | `LoginActivity` stores the token in `SharedPreferences` after XOR/Base64 obfuscation. This is not secure storage. |
| OAuth token transmitted to a non-GitHub host | **NO evidence found** | The only token-bearing app request found is the `Authorization` header to `api.github.com/user`. |
| Username transmitted by app-owned code | **NO evidence found** | The username is parsed from GitHub's `/user` response and stored locally. The WebView's GitHub login form is outside app-owned request serialization. |
| Device information transmitted | **NO explicit app-collected device data found** | No app-owned collection of device ID, IMEI, model, serial, Wi-Fi identifiers, location, contacts, SMS, microphone, or camera data was found. WebView/network stacks can still send normal implicit connection/browser metadata. |
| Configuration transmitted automatically | **NO** | Mod Manager configuration is stored locally. Export uses a user-selected Android share target only after explicit user action. |
| Diagnostics transmitted automatically | **NO** | Diagnostics are generated locally. Export uses `ACTION_SEND` and a chooser; any later transmission depends on the app the user selects. |

The native `libNGI.so` libraries statically expose `Java_nika_ngipro_LoginActivity_getConfigValue` and contain labels for `PREFS_NAME`, `REDIRECT_URI`, `CLIENT_ID`, `CLIENT_SECRET`, and `KEY_TOKEN`. These labels and the decryption/configuration bridge were observed; secret values were deliberately not printed.

## D. Telegram

- Telegram present: **NO evidence found**
- Telegram used for data transmission: **NO evidence found**
- Bot API present: **NO evidence found**
- `api.telegram.org`, `sendMessage`, `sendDocument`, `sendPhoto`, `sendFile`, `t.me`, and `telegram.me`: **not found** in app source, recovered DEX strings, resources, assets, or native strings.

Discord, Firebase, webhook, analytics, and crash-reporting endpoints were likewise not found. `libcrash.so` is a local native crash-guard library; no reporting endpoint or network import was found.

## E. Custom/developer servers

No statically identified non-GitHub hostname was found.

The following developer-controlled behaviors remain relevant:

1. The update metadata is read from a developer-controlled GitHub repository path. Its `lib_url` value is dynamic and is opened through `ACTION_VIEW` without a host allowlist or signature verification.
2. The avatar URL is accepted from the GitHub API response and passed to Glide without an application host allowlist.
3. The WebView allows non-callback navigation to continue, so remote page content can cause the WebView to visit URLs not present in the static application source. No evidence was found that the app captures or forwards credentials from those pages.

No request was made to the update URL, dynamic update link, avatar URL, or any other discovered destination.

## F. Native networking

### Native libraries inspected

- `libNGI.so`
- `libngi_pro_engine.so`
- `libsdkgen.so`
- `libextractor.so`
- `libcrash.so`
- `libc++_shared.so`
- zero-byte `libNeoLibDumper.so` placeholder

### Findings

- `libNGI.so` contains OAuth/configuration key labels and a native configuration/decryption bridge, but no socket, connect, send, receive, DNS, curl, SSL, or TLS imports.
- `libngi_pro_engine.so` contains generic `http://`, `https://`, `secret`, and `token` strings. No concrete hostname was present, and no socket/connect/DNS/curl/SSL/TLS imports were found. This is **possible generic parsing/string handling**, not confirmed network communication.
- `libc++_shared.so` imports `sendfile`, which is a local file-transfer syscall and not evidence of an Internet request.
- No native URL, IP address, Telegram domain, webhook domain, or custom server was statically identified.

The bundled dependency source contains `androidx.constraintlayout.core.motion.utils.Utils.socketSend`, which can write to `127.0.0.1:5327`. No app call site to `socketSend` was found. This is a possible unused local debug hook, not an external data destination.

## G. Runtime observations

No runtime network test was performed.

Reason:

- No authorized Android device or emulator was available.
- No `adb` or packet-capture environment was available.
- No credentials, real or synthetic, were entered.
- No discovered server was contacted.

Therefore there are no runtime-confirmed request hosts, methods, payloads, or background transmissions to report.

Static review shows that opening the Mod Manager, saving configuration, restoring a backup, or generating diagnostics uses local storage and JSON serialization. Export actions hand data to an Android chooser only after explicit user action; they do not perform direct HTTP requests.

## H. Risk assessment

| Finding | Risk | Reason |
| --- | --- | --- |
| OAuth token stored with XOR/Base64 in `SharedPreferences` | **High** | Reversible obfuscation is not secure storage. An attacker with app data or the APK can recover the token. |
| OAuth client secret packaged in the native APK | **High** | A client secret embedded in a client application cannot be kept confidential and should be replaced with a backend-mediated exchange. |
| In-app WebView OAuth with JavaScript/DOM storage and no strict origin allowlist | **Medium** | The app does not capture the password in source, but remote WebView navigation is broader than a narrowly allowlisted OAuth flow. |
| Dynamic update URL opened without signature/host validation | **Medium** | A changed update JSON can point the user to an arbitrary URL or download location. No automatic user-data transmission was observed. |
| `usesCleartextTraffic="true"` | **Medium** | Cleartext communication is permitted even though the fixed application endpoints found are HTTPS. |
| Broad storage permissions and enabled backup/debuggable flags | **Medium** | These expand local data exposure; they were declared in the recovered manifest but were not observed sending data externally. |
| Dynamic avatar URL loaded by Glide | **Low/Medium** | It can fetch a host supplied by the GitHub API response, but no app token or account secret is added to that request. |
| User-initiated configuration/diagnostic sharing | **Low** | Data can leave the app only through the user-selected share target; no automatic network upload exists in the implementation. |
| Unused dependency localhost debug socket | **Low / possible** | A static helper can write to `127.0.0.1:5327`, but no app call site was found. |

## Android permissions and capabilities

| Capability | Declared | App-owned use observed |
| --- | --- | --- |
| `INTERNET` | Yes | OAuth, GitHub API, update check, WebView, and avatar loading |
| `ACCESS_NETWORK_STATE` | Yes | Available to dependency connectivity helpers; no direct app data upload use |
| `ACCESS_WIFI_STATE` | No | Not observed |
| Clipboard permission/API | No manifest permission | Only generic dependency paste/input support; no app-owned clipboard export flow |
| Read/write external storage | Yes | File selection, APK/ELF/DEX analysis, local file operations |
| `MANAGE_EXTERNAL_STORAGE` | Yes | Broad storage capability declared; actual runtime use requires platform/user authorization |
| Contacts | No | Not observed |
| SMS | No | Not observed |
| Phone/device identifiers | No | Not observed |
| Location | No | Not observed in app-owned code |
| Microphone | No | Not observed |
| Camera | No | Not observed |
| Notifications | No notification permission | Not used for reporting or analytics |

## Overall conclusion

**Confirmed:** the application communicates with GitHub for OAuth authorization, token exchange, user-profile retrieval, and developer-controlled update metadata. It stores the OAuth token locally using reversible obfuscation and uses it for the GitHub user API request.

**Not found:** Telegram, Discord, Firebase, webhooks, analytics, crash-reporting servers, custom hard-coded IPs, native socket-based exfiltration, automatic Mod Manager/diagnostics upload, or token transmission to a non-GitHub host.

**Possible but unconfirmed:** WebView navigation to remote hosts returned by page content, avatar fetching from a dynamic URL, an arbitrary update link supplied by update metadata, and an unused localhost debug socket in bundled dependency code.

No application modification, credential interception, server contact, test-data upload, authentication bypass, hook, or credential logging was performed.
