package br.com.helpdev.validador

import android.os.Bundle
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class FragValidadores : FragValidadoresAbs() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mAdView = view.findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("ED22D380D881867345E6858189EB4D35")
                .build()
        mAdView.loadAd(adRequest)
    }
}
