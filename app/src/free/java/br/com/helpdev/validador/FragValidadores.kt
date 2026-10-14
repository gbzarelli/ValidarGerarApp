package br.com.helpdev.validador

import android.os.Bundle
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class FragValidadores : FragValidadoresAbs() {

    private var adView: AdView? = null
    private var consentCoordinator: FreeAdConsentCoordinator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentAdView = view.findViewById<AdView>(R.id.adView)
        adView = currentAdView

        val activity = requireActivity()
        val coordinator = FreeAdConsentCoordinator(
                GoogleUmpConsentGateway(activity),
                GoogleMobileAdsInitializer(activity.applicationContext)
        )
        consentCoordinator = coordinator
        coordinator.updateConsentAndContinue {
            if (adView === currentAdView) currentAdView.loadAd(AdRequest.Builder().build())
        }
    }

    override fun onResume() {
        super.onResume()
        adView?.resume()
    }

    override fun onPause() {
        adView?.pause()
        super.onPause()
    }

    override fun onDestroyView() {
        consentCoordinator?.cancel()
        consentCoordinator = null
        adView?.destroy()
        adView = null
        super.onDestroyView()
    }
}
