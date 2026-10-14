# Validar e Gerar v2 — signing and artifact checklist

## Upload certificates

These are new upload keys for the v2 Play app records. Play App Signing will generate/manage separate app-signing keys after enrollment; do not treat these upload fingerprints as the Play app-signing fingerprints.

| Flavor | Package | Alias | Upload certificate SHA-256 |
| --- | --- | --- | --- |
| Free | `br.com.helpdev.validador.v2.free` | `validador-v2-free-upload` | `17:E7:4E:25:D3:B5:7D:40:DA:50:D8:A2:46:CB:FC:75:47:E8:36:76:89:4A:02:7E:63:B0:8E:FE:0F:7A:50:F7` |
| Paid | `br.com.helpdev.validador.v2` | `validador-v2-paid-upload` | `CD:53:B9:67:ED:BE:BE:A4:CB:A3:D8:7A:CB:C5:58:98:F5:AD:98:0A:BA:DC:CE:46:F4:FC:FE:95:0D:38:E8:8F` |

## Local secret storage

- Keystores and password files live outside Git under `/Volumes/gbz-ext/repositories/helpdev/android-keys/`.
- The local, non-secret path/alias configuration is `validador-v2-signing.properties` in that same directory.
- Passwords are in hidden per-flavor files with mode `0600`; do not print, commit, email, or upload them. Keep encrypted offline backup of each keystore and its matching password file.
- From the original repository checkout, `../android-keys` is the default lookup. For another checkout/worktree, set `ANDROID_KEYS_DIR` to that directory before running Gradle.
- Debug builds do not require these files. Signed release artifact tasks fail early with an actionable message if either flavor's signing inputs are missing.

## Local validation artifacts

- [x] Run `ANDROID_KEYS_DIR=/Volumes/gbz-ext/repositories/helpdev/android-keys ./gradlew bundleFreeRelease bundlePaidRelease`.
- [x] Inspect package/version/SDK identities and signing in both AABs; confirm ads/UMP appear only in free.
- [ ] Run release instrumentation on API 24 and API 36. The API 35 minified APK launch succeeded for both flavors; API 24/36 images are unavailable, and the debug instrumentation APK cannot load all test-runner classes against the minified release target.
- [x] The earlier free validation AAB used Google's official sample AdMob App ID and banner unit ID. API 35 release smoke testing displayed the labeled Google test banner. That bundle is stale for submission and must not be uploaded to Play.
- [x] Rebuild and inspect final AABs using production IDs in `app/src/free/res/values/ads.xml`, with Google's sample IDs overridden only in `app/src/freeDebug/res/values/ads.xml`. The free release AAB has production IDs only and paid has neither AdMob IDs nor Mobile Ads/UMP runtime dependencies.
- [ ] Complete the free AdMob consent message and wait for app approval before any ad-supported test upload.
- [ ] In Play App Signing enrollment, provide the matching upload certificate for each package and separately record the Play-generated app-signing certificate fingerprints.

## Artifact outputs

Expected AAB paths after a successful local validation build:

- `app/build/outputs/bundle/freeRelease/app-free-release.aab`
- `app/build/outputs/bundle/paidRelease/app-paid-release.aab`
