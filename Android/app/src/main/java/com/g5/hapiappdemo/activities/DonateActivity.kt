package com.g5.hapiappdemo.activities

import android.app.FragmentManager
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.g5.hapiappdemo.R
import com.g5.hapiappdemo.databinding.ActivityOutAppBinding
import com.g5.hapiappdemo.helpers.SiteBrowser

class DonateActivity : BaseActivity() {
    private lateinit var binding: ActivityOutAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOutAppBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val toolbar: Toolbar = findViewById(R.id.toolbar_custom)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = resources.getString(R.string.donate_title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val window: Window = this@DonateActivity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@DonateActivity, R.color.MMPrimary)

        val webView = binding.webview

        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        webSettings.setSupportZoom(true)
        webSettings.defaultTextEncodingName = "utf-8"

        webView.webViewClient = SiteBrowser()
        webView.loadUrl(getString(R.string.donate_url))

        Toast.makeText(this@DonateActivity, resources.getString(R.string.donate_warning), Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        val fm: FragmentManager = fragmentManager
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}