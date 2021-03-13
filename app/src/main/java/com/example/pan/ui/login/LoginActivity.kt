package com.example.pan.ui.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.pan.data.Account
import com.example.pan.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        val loginView = binding.loginView
        val settings = loginView.settings
        settings.javaScriptEnabled = true
        loginView.loadUrl("https://openapi.baidu.com/oauth/2.0/authorize?response_type=token&client_id=yGey4TTRWdoglLxRz0X0fBMxYozXweN9&redirect_uri=oob&scope=basic,netdisk&display=mobile")
        loginView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                Log.d("baidu oauth", request?.url.toString())
                request?.url?.fragment?.split("&")?.forEach {
                    if (it.contains("access_token")) {
                        Log.d("baidu oauth", it)
                        Account.setAccessToken(it.split("=")[1])
                        this@LoginActivity.finish()
                    }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }

        }
    }


}