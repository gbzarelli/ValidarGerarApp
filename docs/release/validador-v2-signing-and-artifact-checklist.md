# Validador de documentos V3 — signing and artifact checklist

## Upload certificates

The existing Free upload key is reused to upload the new V3 app. Play App Signing will generate/manage a separate app-signing key for the new app after enrollment; do not treat this upload fingerprint as the Play app-signing fingerprint.

| Edition | Package | Alias | Upload certificate SHA-256 |
| --- | --- | --- | --- |
| Free | `br.com.helpdev.validador.v3` | `validador-v2-free-upload` | `17:E7:4E:25:D3:B5:7D:40:DA:50:D8:A2:46:CB:FC:75:47:E8:36:76:89:4A:02:7E:63:B0:8E:FE:0F:7A:50:F7` |

## Local secret storage

- Keystores and password files live outside Git under `/Volumes/gbz-ext/repositories/helpdev/android-keys/`.
- The local, non-secret path/alias configuration is `validador-v2-signing.properties` in that same directory.
- The password is in a hidden file with mode `0600`; do not print, commit, email, or upload it. Keep an encrypted offline backup of the keystore and matching password file.
- From the original repository checkout, `../android-keys` is the default lookup. For another checkout/worktree, set `ANDROID_KEYS_DIR` to that directory before running Gradle.
- Debug builds do not require these files. Signed release artifact tasks fail early with an actionable message if the Free signing inputs are missing.

## Local validation artifacts

- [ ] Run `ANDROID_KEYS_DIR=/Volumes/gbz-ext/repositories/helpdev/android-keys ./gradlew bundleFreeRelease` after UMP consent setup is ready.
- [ ] Inspect package/version/SDK identity and signing in the Free V3 AAB.
- [ ] Run release instrumentation on API 24 and API 36. The earlier API 35 minified APK launch succeeded for Free; API 24/36 images are unavailable, and the debug instrumentation APK cannot load all test-runner classes against the minified release target.
- [x] The earlier free validation AAB used Google's official sample AdMob App ID and banner unit ID. API 35 release smoke testing displayed the labeled Google test banner. That bundle is stale for submission and must not be uploaded to Play.
- [x] Create a new AdMob app and banner unit for `br.com.helpdev.validador.v3`; production IDs are scoped to Free release resources and Google's sample IDs only to debug.
- [ ] Complete the Free AdMob consent message and link the Play listing for AdMob app review before any ad-supported test upload.
- [ ] In Play App Signing enrollment, provide the matching upload certificate for the new package and separately record the Play-generated app-signing certificate fingerprint.

## Artifact outputs

Expected AAB paths after a successful local validation build:

- `app/build/outputs/bundle/freeRelease/app-free-release.aab`
