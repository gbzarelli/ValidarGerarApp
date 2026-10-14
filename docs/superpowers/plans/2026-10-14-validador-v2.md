# Validar e Gerar v2 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Modernize the Android app and prepare two new Play releases: paid `br.com.helpdev.validador.v2` and ad-supported `br.com.helpdev.validador.v2.free`.

**Architecture:** Keep one Android application module with `paid` and `free` product flavors. Modernize its toolchain and shared UI, keep Google Mobile Ads and UMP dependencies/configuration exclusive to `free`, and run Play signing, verification, closed testing, and store setup as separate release gates after local artifacts pass.

**Tech Stack:** Android Gradle Plugin 9.4.0 with built-in Kotlin, Gradle 9.6.0, JDK 17, AndroidX, compile/target SDK 36, minimum SDK 24, Google Mobile Ads SDK 25.5.0, Google UMP SDK 4.0.0, JUnit 4, AndroidX Test/Espresso.

**Spec:** `docs/superpowers/specs/2026-10-14-validador-v2-design.md`

## Global Constraints

- Use package `br.com.helpdev.validador.v2` for `paid` and `br.com.helpdev.validador.v2.free` for `free`.
- Keep public listing name “Validar e Gerar Documentos” on both apps when Play accepts it.
- Set `minSdk 24`, `compileSdk 36`, and `targetSdk 36` in both variants.
- Use Gradle 9.6.0, AGP 9.4.0 with built-in Kotlin, and JDK 17. AGP 9+ rejects applying `org.jetbrains.kotlin.android` because Kotlin support is built in.
- Use `com.google.android.gms:play-services-ads:25.5.0` and `com.google.android.ump:user-messaging-platform:4.0.0` only in `free`.
- The paid variant must contain no Mobile Ads or UMP dependency, resources, manifest metadata, view, initialization, or ad request.
- Keep signing keystores and passwords outside Git and never print their contents in command output.
- Keep the old Play apps untouched; publish the new apps through closed testing before production.
- Do not create or change an external Play/AdMob resource, upload an artifact, or submit a release without the action-time confirmation required by the spec and the computer-use policy.

## Review Focus

- Ad SDK initialization without a flavor-specific App ID crashes on launch; test the merged `free` manifest and launch `freeDebug` with the official test App ID.
- Paid accidentally packages the free SDK or ad metadata; assert its runtime dependency graph and merged manifest contain no Mobile Ads/UMP components.
- Consent is unresolved or the UMP request fails; verify test geography and that no ad request occurs until `canRequestAds()` is true.
- Kotlin synthetic view references become stale after view recreation; exercise validate/generate flows after fragment recreation and rotation.
- R8 removes or renames code required by `LibValidadores`; run all document flows against both minified release variants and inspect any required keep rules.

---

### Task 1: Modernize the build toolchain and migrate to AndroidX

**Files:**
- Modify: `gradle/wrapper/gradle-wrapper.properties`
- Modify: `build.gradle`
- Modify: `settings.gradle`
- Modify: `gradle.properties`
- Modify: `app/build.gradle`
- Modify: Kotlin imports in `app/src/main/java/br/com/helpdev/validador/MainActivity.kt`, `app/src/main/java/br/com/helpdev/validador/FragValidadoresAbs.kt`, and `app/src/androidTest/java/br/com/helpdev/validador/*.kt`

**Interfaces:**
- Consumes: current single-module `:app` with `free` and `paid` flavors.
- Produces: a Gradle 9.6.0 build using AGP 9.4.0 built-in Kotlin, with AndroidX runtime and test dependencies; no Android Support artifacts or Kotlin Android Extensions plugin.

- [x] **Step 1: Replace legacy build plugin declarations and Gradle wrapper** with the pinned versions in this task; update plugin and dependency repositories to `google()` and `mavenCentral()`.
- [x] **Step 2: Replace Support libraries and test artifacts with AndroidX dependencies** in `app/build.gradle`; remove `kotlin-android-extensions` and migrate old `android.support.*` imports.
- [x] **Step 3: Replace synthetic fragment view access with view-scoped `findViewById` bindings** in `FragValidadoresAbs`, clearing references when the fragment view is destroyed.
- [x] **Step 4: Configure both flavors with `minSdk 24`, `compileSdk 36`, and `targetSdk 36`**, keeping Java/Kotlin bytecode target at 17.
- [x] **Step 5: Run `./gradlew tasks`**; expected: Gradle configures successfully on JDK 17 and exposes `assembleFreeDebug` and `assemblePaidDebug`.
- [x] **Step 6: Run `./gradlew assembleFreeDebug assemblePaidDebug`**; expected: both debug APKs build without Support Library or Android Extensions resolution errors after replacing the old ads SDK.

### Task 2: Lock new app identity and remove unused Firebase configuration

**Files:**
- Modify: `app/build.gradle`
- Modify: `app/src/main/AndroidManifest.xml`
- Modify: `app/src/free/AndroidManifest.xml`
- Modify: `app/src/paid/AndroidManifest.xml`
- Delete only after source/dependency audit confirms no Firebase service use: `app/src/free/google-services.json`, `app/src/paid/google-services.json`
- Modify: `app/src/free/res/values/strings.xml`, `app/src/paid/res/values/strings.xml`

**Interfaces:**
- Consumes: modernized build from Task 1.
- Produces: exact flavor application IDs, `versionCode 1` and `versionName "2.0.0"` for each new app, with Firebase Google Services removed if unused.

- [x] **Step 1: Audit `app/src` and resolved dependencies for Firebase APIs**; expected: no Firebase service imports or SDK dependencies beyond the obsolete Firebase Ads artifact.
- [x] **Step 2: Replace `firebase-ads` and the Google Services plugin with flavor-specific Mobile Ads configuration in Task 4**; remove Google Services application and obsolete JSON files after confirming Task 2 Step 1.
- [x] **Step 3: Set `paid` application ID to `br.com.helpdev.validador.v2` and `free` application ID to `br.com.helpdev.validador.v2.free`**; initialize `versionCode 1` and `versionName "2.0.0"` for both new package identities.
- [x] **Step 4: Build merged manifests for all four variants** with `./gradlew processFreeDebugMainManifest processPaidDebugMainManifest processFreeReleaseMainManifest processPaidReleaseMainManifest`; expected: exact package IDs and target/min SDK values appear in each manifest.

### Task 3: Add regression coverage for document behavior on both variants

**Files:**
- Modify: `app/src/androidTest/java/br/com/helpdev/validador/MainActivityTest.kt`
- Modify: `app/src/androidTest/java/br/com/helpdev/validador/EspressoUtils.kt`
- Delete or replace: `app/src/test/java/br/com/helpdev/validador/ExampleUnitTest.java`
- Create if useful for pure logic: `app/src/test/java/br/com/helpdev/validador/DocumentValidationTest.kt`

**Interfaces:**
- Consumes: AndroidX test stack from Task 1 and flavor behavior from Task 2.
- Produces: regression assertions for CPF, CNPJ, PIS, credit card and RENAVAM validation/generation; tests run for both `free` and `paid`.

- [x] **Step 1: Add local tests named `validatesCpfSamples`, `validatesCnpjSamples`, `validatesPisSamples`, `validatesCreditCardSamples`, and `validatesRenavamSamples`** using the current `LibValidadores` API; each test asserts one known valid input returns true and one invalid input returns false.
- [x] **Step 2: Run `./gradlew testFreeDebugUnitTest testPaidDebugUnitTest`**; expected: all five local behavior tests pass for both variants.
- [x] **Step 3: Extend Espresso tests with `canValidateAndGenerateEachDocumentType` and `emptyDocumentShowsRequiredError`**; assert generated CPF, CNPJ, PIS, card and RENAVAM values pass their corresponding validator, and empty input displays `R.string.entre_com_valor`.
- [x] **Step 4: Run `./gradlew connectedFreeDebugAndroidTest connectedPaidDebugAndroidTest`** on an API 35 emulator; all 12 tests per flavor passed. API 24/36 are not installed locally and remain unverified.
- [x] **Step 5: Exercise activity/fragment view recreation and system-bar insets** in instrumentation tests; `contentStartsBelowStatusBar` verifies the main page stays below the enforced API 35 status bar. The manifest deliberately locks portrait orientation, so preserve it and verify actions target the recreated view without stale binding references.

### Task 4: Replace the free ad stack and add consent gating

**Files:**
- Modify: `app/build.gradle`
- Modify: `app/src/free/AndroidManifest.xml`
- Modify: `app/src/main/AndroidManifest.xml` only for metadata that is truly common; prefer free-only manifest metadata.
- Modify: `app/src/free/java/br/com/helpdev/validador/FragValidadores.kt`
- Modify: `app/src/free/res/layout/frag_validadores.xml`
- Modify: `app/src/free/res/values/strings.xml`
- Create: `app/src/free/res/values/ads.xml` or flavor-local resource values for the AdMob App ID and banner unit ID.
- Create: `app/src/free/java/br/com/helpdev/validador/FreeAdConsentCoordinator.kt`.
- Modify: `app/src/test` or `app/src/androidTest` for consent/ad configuration tests.

**Interfaces:**
- Consumes: new AdMob App ID, banner unit ID, and consent message configured for the `free` v2 app during Task 7.
- Produces: one free-only consent coordinator that updates consent each launch, shows required forms, exposes whether ads can be requested, and loads the banner only after consent allows it.

- [x] **Step 1: Add `play-services-ads:25.5.0` and `user-messaging-platform:4.0.0` to `freeImplementation` only**; expected: `paidRuntimeClasspath` does not resolve either artifact.
- [x] **Step 2: Introduce an injectable test/production ad configuration** with Google’s documented sample App ID and sample banner unit ID for local validation; production IDs will be supplied from the new AdMob record without committing account secrets.
- [x] **Step 3: Implement `FreeAdConsentCoordinator.updateConsentAndContinue(context, onCanRequestAds)`** using UMP `requestConsentInfoUpdate`, `loadAndShowConsentFormIfRequired`, and `canRequestAds()`; call ad loading at most once per activity/view lifecycle.
- [x] **Step 4: Initialize Mobile Ads once after consent setup** and manage `AdView` lifecycle (`pause`, `resume`, `destroy`) with the owning fragment/activity lifecycle.
- [x] **Step 5: Remove the hard-coded test device and use official Google demo ad IDs for local validation**; final production IDs will be installed only after the new AdMob app exists in Task 7.
- [x] **Step 6: Add `consentCoordinatorRequestsAdsOnlyWhenAllowed` tests** for consent granted, not required, denied/unresolved, and consent request error; expected: ad loading occurs only when `canRequestAds() == true` and at most once per view lifecycle.
- [x] **Step 7: Run `./gradlew assembleFreeDebug assemblePaidDebug testFreeDebugUnitTest testPaidDebugUnitTest`**; expected: free compiles with Mobile Ads and UMP and paid compiles without their manifests/resources/dependencies.
- [x] **Step 8: Launch `freeDebug` with the official Google sample App ID and test banner unit ID**; instrumentation launched `freeDebug` without a missing-App-ID crash, and the API 35 release smoke test displayed Google's labeled test banner. Production serving still requires the new AdMob app/consent message.

### Task 5: Prepare release signing and verify the artifacts locally

**Files:**
- Modify: `app/build.gradle`
- Modify: `.gitignore` if it does not already exclude local signing configuration.
- Create outside Git: two upload keystores and a local properties file, stored under the user-approved `../android-keys` directory.
- Create: `docs/release/validador-v2-signing-and-artifact-checklist.md`

**Interfaces:**
- Consumes: package IDs and release variants from Tasks 2–4.
- Produces: local signed validation AABs for both packages, reproducible signing instructions without secret values, SHA-256 upload certificate fingerprints, and a checklist for Play App Signing setup. The free validation AAB may contain the official sample ad IDs and must not be uploaded; Task 7 builds the final AAB after the production AdMob IDs exist.

- [x] **Step 1: Generate one upload key per new package in `../android-keys`**; do not print passwords or private key material and do not stage the files.
- [x] **Step 2: Wire release signing through an ignored local properties file/environment inputs**; expected: missing local credentials fail with a clear message and debug variants remain buildable.
- [x] **Step 3: Run `./gradlew bundleFreeRelease bundlePaidRelease`**; expected: signed local validation `.aab` artifacts exist for both variants; the free AAB uses only official sample ad IDs and is not for upload.
- [x] **Step 4: Inspect each AAB with `bundletool dump manifest` and dependency reports**; assert package IDs, versionCode 1, versionName 2.0.0, minSdk 24, targetSdk 36, free-only ad metadata, and no Mobile Ads/UMP classes or metadata in paid.
- [ ] **Step 5: Run release instrumentation on API 24 and API 36**; the AGP project does not create `connected*ReleaseAndroidTest` tasks. A manual API 35 launch of both minified release APKs succeeded; manually pairing debug test APKs with release targets fails in AndroidX/Kotlin test-runner class loading before tests execute. API 24 and 36 system images are unavailable locally.
- [x] **Step 6: Record only certificate fingerprints and storage/backup instructions** in the checklist; never record passwords or private key bytes.

### Task 6: Create and configure the new Play apps and signing identities

**Files:**
- Create: `docs/release/validador-v2-play-console-checklist.md`
- Modify: `docs/release/validador-v2-signing-and-artifact-checklist.md`

**Interfaces:**
- Consumes: checked release AABs and upload certificate fingerprints from Task 5.
- Produces: two Play app records, one per exact package, with store setup started; package names and signing enrollment are ready for final AAB delivery in Task 8.

- [x] **Step 1: Search both exact package names in the developer account and confirm the names remain available globally during app creation**; both package names were available at creation time.
- [x] **Step 2: Create the paid Play app as `br.com.helpdev.validador.v2` and the free Play app as `br.com.helpdev.validador.v2.free`**; both draft records use the public title “Validar e Gerar Documentos” and default language Portuguese (Brazil).
- [x] **Step 3: Complete initial store setup fields that do not depend on an uploaded AAB**; both package/title records are saved as drafts. Listing, pricing, and policy declarations remain incomplete.
- [ ] **Step 4: Record the Play App Signing and Android developer verification requirements shown for each new app**; defer signing enrollment and package/certificate registration until the final upload provides the authoritative certificate data in Task 8.

### Task 7: Complete AdMob and policy/store declarations

**Files:**
- Create: `docs/release/validador-v2-play-listing.md`
- Modify: `README.md` to point at the new free Play URL after the app exists.
- Modify: `app/src/free/res/values/ads.xml` with production IDs only after AdMob has created them.

**Interfaces:**
- Consumes: free app package and Play listing from Task 6, and current product behavior from Task 3.
- Produces: verified AdMob `free` app, production banner unit, consent message, accurate Play declarations and listing materials for both apps.

- [ ] **Step 1: Prepare listing copy and declaration answers** for each app, including title, short/full description, screenshots, content rating, target audience, ads declaration, privacy policy URL, and data safety disclosure based on the final AAB dependency/data audit.
- [x] **Step 2: Create a new Android AdMob app for `br.com.helpdev.validador.v2.free` and a banner unit**; created `Validar e Gerar Documentos V2 Free` and `Banner principal V2 Free` without changing the existing app/unit. AdMob App ID: `ca-app-pub-4334384724173584~9198482481`; banner unit ID: `ca-app-pub-4334384724173584/2132626660`.
- [ ] **Step 3: Create/configure the region-appropriate consent message in AdMob Privacy & messaging**; blocked because the embedded AdMob consent editor renders a blank page and its controls have zero-size bounds. Resume only when the editor is visibly usable; target the new free V2 app only.
- [x] **Step 4: Set production App ID and unit ID only in free release resources**; free debug overrides them with Google's official test IDs, and paid contains neither set.
- [ ] **Step 5: Complete the Play store listing, policy declarations, app content, data safety, countries, and paid price** according to the two distinct app behaviors.
- [ ] **Step 6: Verify AdMob app status and policy center**; if app verification is pending, use test ads only and keep production rollout gated until approval.
- [x] **Step 7: Rebuild both final signed release AABs with production free AdMob IDs and inspect merged manifests/dependency reports again**; free release AAB contains the new App ID/banner ID and no sample ID, free debug resolves only Google's sample IDs, and paid contains neither AdMob ID nor ad SDK/UMP runtime dependency. Consent editor setup and AdMob app approval remain gates before any upload.

### Task 8: Closed testing, pre-launch review, and production release

**Files:**
- Update: `docs/release/validador-v2-play-console-checklist.md`
- Update: `docs/release/validador-v2-play-listing.md` with final track and release identifiers.

**Interfaces:**
- Consumes: accepted AABs, signing, AdMob configuration, and completed Play setup from Tasks 5–7.
- Produces: both apps approved through closed testing, production submissions, and confirmed public Play listings.

- [ ] **Step 1: Enable Play App Signing, enroll the upload certificates, register both package names/certificates in Android developer verification, then upload the final paid and free AABs to closed testing tracks**; configure tester access and country availability as shown in the account.
- [ ] **Step 2: Install from Play closed-test links on API 24 and API 36 devices**; verify validate/generate flows, clipboard, rotation, consent UI, banner loading, and no ad view/request in paid.
- [ ] **Step 3: Review each Play pre-launch report, Android vitals, policy status, and AdMob policy center**; resolve every blocking warning before continuing.
- [ ] **Step 4: Confirm required closed-testing duration and tester counts shown by Play Console**; preserve evidence that requirements are satisfied.
- [ ] **Step 5: Request final user confirmation immediately before submitting each release to production review**; expected: confirmation names the app, package, track, and exact AAB.
- [ ] **Step 6: After Google approval, verify both public listing URLs, installability, paid purchase flow, and free ad consent/serving**; record final version and rollout status.

---

## Official references used for version and policy baselines

- AGP 9.4.0 compatibility and Gradle/JDK requirements: https://developer.android.com/build/releases/agp-9-4-0-release-notes
- AGP 9.4.0 and Gradle 9.6.0 configuration: https://developer.android.com/build/releases/about-agp
- Play target API requirement (API 36 from August 31, 2026): https://developer.android.com/google/play/requirements/target-sdk
- Google Mobile Ads SDK 25.5.0 release notes and minimum API 24: https://developers.google.com/admob/android/rel-notes
- Google Mobile Ads setup and App ID requirement: https://developers.google.com/admob/android/quick-start
- Google UMP SDK 4.0.0 and consent flow: https://developers.google.com/admob/android/privacy

