package com.g5.hapiappdemo.helpers

import android.webkit.WebView
import android.webkit.WebViewClient


class SiteBrowser : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        return true
    }
}