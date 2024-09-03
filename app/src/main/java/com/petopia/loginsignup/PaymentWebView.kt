package com.petopia.loginsignup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient


class PaymentWebView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_web_view)

        // Get a reference to the WebView
        val webView = findViewById<WebView>(R.id.webView)

        // Configure WebView settings
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.setSupportMultipleWindows(true)
        webView.settings.loadsImagesAutomatically = true
        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true

        // Set a WebViewClient to handle page loading
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                // Page has finished loading, you can handle further actions here if needed
            }
        }

        // Retrieve the redirect URL from the intent extras
        val redirectUrl = intent.getStringExtra("redirectUrl")

        // Load the redirect URL in the WebView
        if (redirectUrl != null) {
            webView.loadUrl(redirectUrl)
        } else {
            // Handle the case where the redirect URL is missing
            // You can display an error message or take appropriate action
        }
    }
}

