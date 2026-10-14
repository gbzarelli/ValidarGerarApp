# Validar e Gerar v2 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Modernize the Android app and prepare two new Play releases: paid `br.com.helpdev.validador.v2` and ad-supported `br.com.helpdev.validador.v2.free`.

**Architecture:** Keep one Android application module with `paid` and `free` product flavors. Modernize its toolchain and shared UI, keep Google Mobile Ads and UMP dependencies/configuration exclusive to `free`, and run Play signing, verification, closed testing, and store setup as separate release gates after local artifacts pass.

**Tech Stack:** Android Gradle Plugin 9.4.0, Gradle 9.6.0, Kotlin Gradle Plugin 2.4.10, JDK 17, AndroidX, compile/target SDK 36, minimum SDK 24, Google Mobile Ads SDK 25.5.0, Google UMP SDK 4.0.0, JUnit 4, AndroidX Test/Espresso.

**Spec:** `docs/superpowers/specs/2026-10-14-validador-v2-design.md`

## Global Constraints

- Use package `br.com.helpdev.validador.v2` for `paid` and `br.com.helpdev.validador.v2.free` for `free`.
- Keep public listing name “Validar e Gerar Documentos” on both apps when Play accepts it.
- Set `minSdk 24`, `compileSdk 36`, and `targetSdk 36` in both variants.
- Use Gradle 9.6.0, AGP 9.4.0, Kotlin Gradle Plugin 2.4.10, and JDK 17.
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
- Produces: a Gradle 9.6.0 build using AGP 9.4.0 and Kotlin 2.4.10, with AndroidX runtime and test dependencies; no Android Support artifacts or Kotlin Android Extensions plugin.

- [ ] **Step 1: Replace legacy build plugin declarations and Gradle wrapper** with the pinned versions in this task; update plugin and dependency repositories to `google()` and `mavenCentral()`.
- [ ] **Step 2: Replace Support libraries and test artifacts with AndroidX dependencies** in `app/build.gradle`; remove `kotlin-android-extensions` and migrate old `android.support.*` imports.
- [ ] **Step 3: Replace synthetic fragment view access with view-scoped `findViewById` bindings** in `FragValidadoresAbs`, clearing references when the fragment view is destroyed.
- [ ] **Step 4: Configure both flavors with `minSdk 24`, `compileSdk 36`, and `targetSdk 36`**, keeping Java/Kotlin bytecode target at 17.
- [ ] **Step 5: Run `./gradlew tasks`**; expected: Gradle configures successfully on JDK 17 and exposes `assembleFreeDebug` and `assemblePaidDebug`.
- [ ] **Step 6: Run `./gradlew assembleFreeDebug assemblePaidDebug`**; expected: both debug APKs build without Support Library or Android Extensions resolution errors.

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

- [ ] **Step 1: Audit `app/src` and resolved dependencies for Firebase APIs**; expected: no Firebase service imports or SDK dependencies beyond the obsolete Firebase Ads artifact.
- [ ] **Step 2: Replace `firebase-ads` and the Google Services plugin with flavor-specific Mobile Ads configuration in Task 4**; remove Google Services application and obsolete JSON files after confirming Task 2 Step 1.
- [ ] **Step 3: Set `paid` application ID to `br.com.helpdev.validador.v2` and `free` application ID to `br.com.helpdev.validador.v2.free`**; initialize `versionCode 1` and `versionName "2.0.0"` for both new package identities.
- [ ] **Step 4: Build merged manifests for all four variants** with `./gradlew processFreeDebugMainManifest processPaidDebugMainManifest processFreeReleaseMainManifest processPaidReleaseMainManifest`; expected: exact package IDs and target/min SDK values appear in each manifest.

### Task 3: Add regression coverage for document behavior on both variants

**Files:**
- Modify: `app/src/androidTest/java/br/com/helpdev/validador/MainActivityTest.kt`
- Modify: `app/src/androidTest/java/br/com/helpdev/validador/EspressoUtils.kt`
- Delete or replace: `app/src/test/java/br/com/helpdev/validador/ExampleUnitTest.java`
- Create if useful for pure logic: `app/src/test/java/br/com/helpdev/validador/DocumentValidationTest.kt`

**Interfaces:**
- Consumes: AndroidX test stack from Task 1 and flavor behavior from Task 2.
- Produces: regression assertions for CPF, CNPJ, PIS, credit card and RENAVAM validation/generation; tests run for both `free` and `paid`.

- [ ] **Step 1: Add local tests named `validatesCpfSamples`, `validatesCnpjSamples`, `validatesPisSamples`, `validatesCreditCardSamples`, and `validatesRenavamSamples`** using the current `LibValidadores` API; each test asserts one known valid input returns true and one invalid input returns false.
- [ ] **Step 2: Run `./gradlew testFreeDebugUnitTest testPaidDebugUnitTest`**; expected: all five local behavior tests pass for both variants.
- [ ] **Step 3: Extend Espresso tests with `canValidateAndGenerateEachDocumentType` and `emptyDocumentShowsRequiredError`**; assert generated CPF, CNPJ, PIS, card and RENAVAM values pass their corresponding validator, and empty input displays `R.string.entre_com_valor`.
- [ ] **Step 4: Run `./gradlew connectedFreeDebugAndroidTest connectedPaidDebugAndroidTest`** on an API 24 emulator; expected: all existing and added interactions pass for both flavors.
- [ ] **Step 5: Exercise fragment view recreation and rotation** in instrumentation tests; expected: actions target the current view and do not throw or leak stale binding references.

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

- [ ] **Step 1: Add `play-services-ads:25.5.0` and `user-messaging-platform:4.0.0` to `freeImplementation` only**; expected: `paidRuntimeClasspath` does not resolve either artifact.
- [ ] **Step 2: Introduce an injectable test/production ad configuration** with Google’s documented sample App ID and sample banner unit ID in debug/test, and production IDs supplied from the new AdMob record without committing credentials or account secrets.
- [ ] **Step 3: Implement `FreeAdConsentCoordinator.updateConsentAndContinue(context, onCanRequestAds)`** using UMP `requestConsentInfoUpdate`, `loadAndShowConsentFormIfRequired`, and `canRequestAds()`; call ad loading at most once per activity/view lifecycle.
- [ ] **Step 4: Initialize Mobile Ads once after consent setup** and manage `AdView` lifecycle (`pause`, `resume`, `destroy`) with the owning fragment/activity lifecycle.
- [ ] **Step 5: Remove the hard-coded production test device and use official test ad IDs/test-device configuration only in debug builds**; expected: release builds contain no developer test-device identifier.
- [ ] **Step 6: Add `consentCoordinatorRequestsAdsOnlyWhenAllowed` tests** for consent granted, not required, denied/unresolved, and consent request error; expected: ad loading occurs only when `canRequestAds() == true` and at most once per view lifecycle.
- [ ] **Step 7: Run `./gradlew assembleFreeDebug assemblePaidDebug testFreeDebugUnitTest testPaidDebugUnitTest`**; expected: free compiles with Mobile Ads and UMP and paid compiles without their manifests/resources/dependencies.
- [ ] **Step 8: Launch `freeDebug` with the official Google sample App ID and test banner unit ID**; expected: no missing App ID crash and the test banner is requested only after the consent callback allows ads.

### Task 5: Prepare release signing and verify the artifacts locally

**Files:**
- Modify: `app/build.gradle`
- Modify: `.gitignore` if it does not already exclude local signing configuration.
- Create outside Git: two upload keystores and a local properties file, stored under the user-approved `../android-keys` directory.
- Create: `docs/release/validador-v2-signing-and-artifact-checklist.md`

**Interfaces:**
- Consumes: package IDs and release variants from Tasks 2–4.
- Produces: signed release AABs for both packages, reproducible signing instructions without secret values, SHA-256 certificate fingerprints, and a checklist for Play App Signing setup.

- [ ] **Step 1: Generate one upload key per new package in `../android-keys`**; do not print passwords or private key material and do not stage the files.
- [ ] **Step 2: Wire release signing through an ignored local properties file/environment inputs**; expected: missing local credentials fail with a clear message and debug variants remain buildable.
- [ ] **Step 3: Run `./gradlew bundleFreeRelease bundlePaidRelease`**; expected: signed `.aab` artifacts exist for both variants.
- [ ] **Step 4: Inspect each AAB with `bundletool dump manifest` and dependency reports**; assert package IDs, versionCode 1, versionName 2.0.0, minSdk 24, targetSdk 36, free-only ad metadata, and no Mobile Ads/UMP classes or metadata in paid.
- [ ] **Step 5: Run `./gradlew connectedFreeReleaseAndroidTest connectedPaidReleaseAndroidTest`** on API 24 and API 36 devices/emulators; expected: core validate/generate behavior passes with R8/minification enabled.
- [ ] **Step 6: Record only certificate fingerprints and storage/backup instructions** in the checklist; never record passwords or private key bytes.

### Task 6: Create and configure the new Play apps and signing identities

**Files:**
- Create: `docs/release/validador-v2-play-console-checklist.md`
- Modify: `docs/release/validador-v2-signing-and-artifact-checklist.md`

**Interfaces:**
- Consumes: checked release AABs and upload certificate fingerprints from Task 5.
- Produces: two Play app records, one per exact package, Play App Signing enabled for each, and both signing identities ready for closed testing and Android developer verification.

- [ ] **Step 1: Search both exact package names in the developer account and confirm the names remain available globally during app creation**; if either is unavailable, stop before choosing an alternate package.
- [ ] **Step 2: Create the paid Play app as `br.com.helpdev.validador.v2` and the free Play app as `br.com.helpdev.validador.v2.free`**; enter public title “Validar e Gerar Documentos” where accepted by Play.
- [ ] **Step 3: Enable Play App Signing for both new apps and enroll the provided upload certificate for each**; save the Play app-signing certificate fingerprints separately from upload fingerprints.
- [ ] **Step 4: Register both package names and required signing certificates in the Android developer verification flow**; expected: the account shows registered status for both package IDs.
- [ ] **Step 5: Confirm the new app records accept the inspected AAB identities without submitting a review**; expected: package and certificate match in each draft release.

### Task 7: Complete AdMob and policy/store declarations

**Files:**
- Create: `docs/release/validador-v2-play-listing.md`
- Modify: `README.md` to point at the new free Play URL after the app exists.
- Modify: `app/src/free/res/values/ads.xml` with production IDs only after AdMob has created them.

**Interfaces:**
- Consumes: free app package and Play listing from Task 6, and current product behavior from Task 3.
- Produces: verified AdMob `free` app, production banner unit, consent message, accurate Play declarations and listing materials for both apps.

- [ ] **Step 1: Prepare listing copy and declaration answers** for each app, including title, short/full description, screenshots, content rating, target audience, ads declaration, privacy policy URL, and data safety disclosure based on the final AAB dependency/data audit.
- [ ] **Step 2: Create a new Android AdMob app for `br.com.helpdev.validador.v2.free` and a banner unit**; keep the existing old app/unit unchanged.
- [ ] **Step 3: Create/configure the region-appropriate consent message in AdMob Privacy & messaging**; expected: UMP recognizes the message for the new App ID.
- [ ] **Step 4: Set production App ID and unit ID only in free flavor resources**; expected: paid merged manifest and resource table contain neither ID.
- [ ] **Step 5: Complete the Play store listing, policy declarations, app content, data safety, countries, and paid price** according to the two distinct app behaviors.
- [ ] **Step 6: Verify AdMob app status and policy center**; if app verification is pending, use test ads only and keep production rollout gated until approval.

### Task 8: Closed testing, pre-launch review, and production release

**Files:**
- Update: `docs/release/validador-v2-play-console-checklist.md`
- Update: `docs/release/validador-v2-play-listing.md` with final track and release identifiers.

**Interfaces:**
- Consumes: accepted AABs, signing, AdMob configuration, and completed Play setup from Tasks 5–7.
- Produces: both apps approved through closed testing, production submissions, and confirmed public Play listings.

- [ ] **Step 1: Upload the paid and free AABs to closed testing tracks**; configure tester access and country availability as shown in the account.
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

