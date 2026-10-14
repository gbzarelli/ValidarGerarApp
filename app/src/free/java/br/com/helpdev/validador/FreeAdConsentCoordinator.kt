package br.com.helpdev.validador

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform

internal interface ConsentGateway {
    fun updateAndShow(onComplete: (Throwable?) -> Unit)
    fun canRequestAds(): Boolean
}

internal interface AdsInitializer {
    fun initialize(onInitialized: () -> Unit)
}

internal class FreeAdConsentCoordinator(
    private val consentGateway: ConsentGateway,
    private val adsInitializer: AdsInitializer
) {
    private var started = false
    private var consentHandled = false
    private var initializationCompleted = false
    private var cancelled = false

    fun updateConsentAndContinue(onCanRequestAds: () -> Unit) {
        if (started || cancelled) return
        started = true

        consentGateway.updateAndShow { _ ->
            if (cancelled || consentHandled) return@updateAndShow
            consentHandled = true
            if (!consentGateway.canRequestAds()) return@updateAndShow

            adsInitializer.initialize {
                if (cancelled || initializationCompleted) return@initialize
                initializationCompleted = true
                onCanRequestAds()
            }
        }
    }

    fun cancel() {
        cancelled = true
    }
}

internal class GoogleUmpConsentGateway(private val activity: Activity) : ConsentGateway {
    private val consentInformation: ConsentInformation = UserMessagingPlatform.getConsentInformation(activity)

    override fun updateAndShow(onComplete: (Throwable?) -> Unit) {
        val parameters = ConsentRequestParameters.Builder().build()
        consentInformation.requestConsentInfoUpdate(
            activity,
            parameters,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                    onComplete(formError?.let { IllegalStateException(it.message) })
                }
            },
            { requestError -> onComplete(IllegalStateException(requestError.message)) }
        )
    }

    override fun canRequestAds(): Boolean = consentInformation.canRequestAds()
}

internal class GoogleMobileAdsInitializer(context: Context) : AdsInitializer {
    private val applicationContext = context.applicationContext

    override fun initialize(onInitialized: () -> Unit) {
        MobileAds.initialize(applicationContext) { onInitialized() }
    }
}
