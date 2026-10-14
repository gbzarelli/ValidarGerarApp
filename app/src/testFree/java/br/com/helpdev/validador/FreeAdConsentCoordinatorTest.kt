package br.com.helpdev.validador

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FreeAdConsentCoordinatorTest {

    @Test
    fun consentCoordinatorRequestsAdsOnlyWhenAllowed() {
        val consent = FakeConsentGateway(canRequestAds = true)
        val ads = FakeAdsInitializer()
        val coordinator = FreeAdConsentCoordinator(consent, ads)
        var adRequestAllowed = false

        coordinator.updateConsentAndContinue { adRequestAllowed = true }

        assertEquals(1, consent.updateCount)
        assertEquals(0, ads.initializeCount)
        assertFalse(adRequestAllowed)

        consent.complete()

        assertEquals(1, ads.initializeCount)
        assertFalse(adRequestAllowed)
        ads.completeInitialization()
        assertTrue(adRequestAllowed)
    }

    @Test
    fun consentNotRequiredCanRequestAdsAfterUpdate() {
        val consent = FakeConsentGateway(canRequestAds = true)
        val ads = FakeAdsInitializer()
        val coordinator = FreeAdConsentCoordinator(consent, ads)
        var adRequestAllowed = false

        coordinator.updateConsentAndContinue { adRequestAllowed = true }
        consent.complete()
        ads.completeInitialization()

        assertTrue(adRequestAllowed)
        assertEquals(1, ads.initializeCount)
    }

    @Test
    fun deniedOrUnresolvedConsentDoesNotInitializeAds() {
        val consent = FakeConsentGateway(canRequestAds = false)
        val ads = FakeAdsInitializer()
        val coordinator = FreeAdConsentCoordinator(consent, ads)
        var adRequestAllowed = false

        coordinator.updateConsentAndContinue { adRequestAllowed = true }
        consent.complete()

        assertFalse(adRequestAllowed)
        assertEquals(0, ads.initializeCount)
    }

    @Test
    fun consentRequestErrorUsesOnlyPreviouslyAllowedState() {
        val consent = FakeConsentGateway(canRequestAds = false)
        val ads = FakeAdsInitializer()
        val coordinator = FreeAdConsentCoordinator(consent, ads)
        var adRequestAllowed = false

        coordinator.updateConsentAndContinue { adRequestAllowed = true }
        consent.complete(IllegalStateException("request failed"))

        assertFalse(adRequestAllowed)
        assertEquals(0, ads.initializeCount)
    }

    @Test
    fun coordinatorStartsAtMostOnceForOneViewLifecycle() {
        val consent = FakeConsentGateway(canRequestAds = true)
        val ads = FakeAdsInitializer()
        val coordinator = FreeAdConsentCoordinator(consent, ads)
        var allowedCount = 0

        coordinator.updateConsentAndContinue { allowedCount++ }
        coordinator.updateConsentAndContinue { allowedCount++ }
        consent.complete()
        consent.complete()
        ads.completeInitialization()
        ads.completeInitialization()

        assertEquals(1, consent.updateCount)
        assertEquals(1, ads.initializeCount)
        assertEquals(1, allowedCount)
    }

    private class FakeConsentGateway(private val canRequestAds: Boolean) : ConsentGateway {
        var updateCount = 0
            private set
        private var callback: ((Throwable?) -> Unit)? = null

        override fun updateAndShow(onComplete: (Throwable?) -> Unit) {
            updateCount++
            callback = onComplete
        }

        override fun canRequestAds() = canRequestAds

        fun complete(error: Throwable? = null) {
            callback?.invoke(error)
        }
    }

    private class FakeAdsInitializer : AdsInitializer {
        var initializeCount = 0
            private set
        private var callback: (() -> Unit)? = null

        override fun initialize(onInitialized: () -> Unit) {
            initializeCount++
            callback = onInitialized
        }

        fun completeInitialization() {
            callback?.invoke()
        }
    }
}
