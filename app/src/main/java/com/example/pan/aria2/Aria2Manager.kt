package com.example.pan.aria2

import android.util.Log
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.concurrent.thread

object Aria2Manager {

    val mmkv = MMKV.mmkvWithID("ACCOUNT", MMKV.MULTI_PROCESS_MODE)

    fun download(url: String, fileName: String) {
        thread {
            val httpClient = OkHttpClient.Builder().build()

            val option = HashMap<String, Any>()
            option["out"] = fileName

            val map = HashMap<String, Any>()
            map["jsonrpc"] = "2.0"
            map["method"] = "aria2.addUri"
            map["id"] = "QXJpYU5nXzE2MTM4OTMxMjBfMC4wODg4ODM0MTIzNDc0Mzc4"
            map["params"] =
                arrayListOf(arrayListOf("$url&access_token=123.b3144a2ffcf44d5008d34b4eba5083ee.Y7c31a9oZoQ0WT_lt8QM6tT4yko1ZexHZQTuNAw.sbeiZQ"), option)

            val request = Request.Builder()
                .url("http://127.0.0.1:6800/jsonrpc")
                .post(Gson().toJson(map).toRequestBody())
                .build()

            val response = httpClient.newCall(request).execute()

            response.body?.string()?.let { Log.d("download", it) }
        }


    }
}