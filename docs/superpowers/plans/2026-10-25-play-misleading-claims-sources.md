# Play Misleading Claims Sources Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Correct the V3 listing and app presentation for the Play rejection by making its independent, local-only scope explicit and providing usable official sources.

**Architecture:** Keep the existing V3 Play app/package and validation-only behavior. Add a concise disclaimer and linkable official-source section to the Android screen, align the pt-BR Play listing and linked privacy page, and produce a new signed release artifact with an incremented version code.

**Tech Stack:** Kotlin, Android XML resources, Gradle, Espresso, Google Play Console.

**Spec:** Google Play rejection notice for `br.com.helpdev.validador.v3`: Misleading Claims — Missing Source Link for Government Information, full description (pt-BR), 2026-10-25.

## Global Constraints

- Keep `applicationId` `br.com.helpdev.validador.v3` and the existing Play listing.
- Keep the app limited to local format/check-digit validation; no generation, registry lookup, status check, or ownership claim.
- Retain CPF, CNPJ (numeric and alphanumeric), PIS, and RENAVAM only if their current validators and citations remain accurate.
- The app is independent and not affiliated with any government entity.
- Keep Free/AdMob behavior and the existing consent gate; do not change ad identifiers or bypass consent.
- Do not submit to Play review unless the existing consent/release gate is resolved and release checks pass.

## Review Focus

- App says “valid” could imply official registration: test the visible local-only/independence disclaimer.
- Government source URLs could be malformed or mislabelled: test rendered URL spans and verify each URL loads.
- Alphanumeric CNPJ could regress while adding copy/UI: retain numeric, alphanumeric, and invalid-DV tests.
- A release build could retain a stale package or version: verify manifest/package and `versionCode` on the signed AAB.
- Source list could be obscured by the banner or system bars: verify scrollability and capture a current emulator screen when an AVD is available.

---

### Task 1: Add in-app disclaimer and official source links

**Files:**
- Modify: `app/src/main/res/layout/frag_validadores_abs.xml`
- Modify: `app/src/main/res/values/strings.xml`
- Modify: `app/src/free/res/layout/frag_validadores.xml`
- Modify: `app/src/main/java/br/com/helpdev/validador/FragValidadoresAbs.kt`
- Test: `app/src/androidTest/java/br/com/helpdev/validador/MainActivityTest.kt`

**Interfaces:**
- Use official URLs for CPF (Receita Federal), CNPJ alphanumeric and its check-digit calculation (Receita Federal), PIS/NIT (INSS), and RENAVAM (Senatran service).
- Render named links through `URLSpan` and keep the source list reachable above the ad banner using scrolling.

- [x] Add an Espresso test asserting the disclaimer and all five official-source links are visible/clickable.
- [x] Run `./gradlew connectedFreeDebugAndroidTest` and confirm the new test fails for the missing source section.
- [x] Add the disclaimer/source copy and link handling with `HtmlCompat` plus `LinkMovementMethod`; make the content scroll without covering the banner.
- [x] Re-run the instrumentation test and confirm it passes.

### Task 2: Prepare the V3 Play listing and release artifact

**Files:**
- Modify: `app/build.gradle`
- Modify: `README.md`
- Modify: `docs/release/validador-v2-play-console-checklist.md`

**Interfaces:**
- Keep package `br.com.helpdev.validador.v3`; set version name `3.0.2` and code `3` because code 2 has already been uploaded in an unsubmitted draft.
- Store description must be pt-BR, state independent/local-only behavior, link official government channels including the CNPJ check-digit technical source, retain the privacy-policy URL, and disclose ads.
- Keep distribution Brazil-only and use the existing V3 signing configuration.

- [x] Record pre-change unit/build baseline and verify the Free-only build graph.
- [x] Update release metadata/checklist and verify the listing text against the rejection guidance.
- [x] Run `./gradlew testFreeDebugUnitTest assembleFreeDebug` and `./gradlew bundleFreeRelease`.
- [x] Verify the release bundle package, version, and signing certificate; do not upload debug artifacts.
- [x] Replace the code 2 bundle in the existing V3 production draft with the final signed code 3 AAB, and save the production release draft for Brazil.
- [ ] Submit only after a European UMP consent message is configured and the consent flow is verified; the user previously chose to wait for this gate.

### Task 3: Final policy and release verification

**Files:**
- The linked privacy page is updated in the site repository so its independent/local-only disclaimer and CNPJ technical source match the app and listing.

- [x] Update the public privacy page to state independence/local-only validation and link official services without implying integration.
- [x] Verify every official source URL against the Play listing and Android screen, including the Receita Federal's CNPJ check-digit documentation.
- [x] Capture current UI and an obviously invalid all-zero test value from emulator. Convert screenshots to JPEG/RGB before store upload; avoid exposing a full valid-looking identifier.
- [x] Replace the earlier screenshot containing a full valid test CPF with the all-zero invalid-value screenshot in the Play listing draft.
- [x] Review the resulting diff, run `git diff --check`, and document the UMP consent setup as a release blocker.
