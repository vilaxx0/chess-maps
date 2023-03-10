package com.viliusbucinskas.chessmaps

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.viliusbucinskas.chessmaps.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class ClockActivity : AppCompatActivity() {

    private val nav = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.mapsFragment -> {
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.clockFragment -> {
                val intent = Intent(this, ClockActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.mainActivity -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    private val webView: WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.clock_activity)
        val webView = findViewById<WebView>(R.id.webView)
        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://chessclock.org/")
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigatin_view)
        //bottomNavigation.setSelectedItemId(R.id.home)
        bottomNavigation.setOnNavigationItemSelectedListener(nav)
    }
    override fun onBackPressed() {
        if (webView!!.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}