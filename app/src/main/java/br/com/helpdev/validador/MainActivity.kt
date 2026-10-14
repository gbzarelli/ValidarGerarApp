package br.com.helpdev.validador

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val content = findViewById<View>(R.id.activity_main)
        val initialPaddingLeft = content.paddingLeft
        val initialPaddingTop = content.paddingTop
        val initialPaddingRight = content.paddingRight
        val initialPaddingBottom = content.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(content) { view, windowInsets ->
            val systemBars = windowInsets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            view.setPadding(
                initialPaddingLeft + systemBars.left,
                initialPaddingTop + systemBars.top,
                initialPaddingRight + systemBars.right,
                initialPaddingBottom + systemBars.bottom
            )
            windowInsets
        }
        ViewCompat.requestApplyInsets(content)
    }
}
