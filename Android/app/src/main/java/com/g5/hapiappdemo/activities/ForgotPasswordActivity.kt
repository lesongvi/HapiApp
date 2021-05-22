package com.g5.hapiappdemo.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.g5.hapiappdemo.R
import com.g5.hapiappdemo.databinding.ActivityOutAppBinding
import com.g5.hapiappdemo.helpers.SiteBrowser


class ForgotPasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityOutAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOutAppBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val toolbar: Toolbar = findViewById(R.id.toolbar_custom)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = resources.getString(R.string.forgot_password)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val window: Window = this@ForgotPasswordActivity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@ForgotPasswordActivity, R.color.MMPrimary)

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
        webView.loadUrl(getString(R.string.forgot_password_url))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                onBackPressed()
        }
        return true
    }


    override fun onBackPressed() {
        finish()
    }
}