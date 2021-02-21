package com.example.pan.ui.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.pan.R
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.Cookie
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

class LoginActivity : AppCompatActivity() {

    val cookiePersistor by lazy { SharedPrefsCookiePersistor(applicationContext) }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginView = findViewById<WebView>(R.id.login_view)

        val settings = loginView.settings

        settings.javaScriptEnabled = true
        settings.userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36"

        loginView.loadUrl("https://pan.baidu.com/")

        loginView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                val cookie = CookieManager.getInstance().getCookie("pan.baidu.com")
                if (cookie != null && (cookie.contains("BDUSS=")) && (url.contains("pan.baidu.com/disk/home"))) {
                    val cookies = arrayListOf<Cookie>()
                    for (item in cookie.split(";")) {
                        "https://pan.baidu.com/".toHttpUrlOrNull()?.let { httpUrl ->
                            Cookie.parse(httpUrl, item)?.let {
                                cookies.add(
                                    it
                                )
                            }
                        }
                    }
                    print(cookies)
                    cookiePersistor.saveAll(cookies)

                }
            }

        }


    }

}