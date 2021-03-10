package com.example.pan.aria2

import android.util.Log
import com.example.pan.model.Aria2Response
import com.example.pan.model.TaskInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

object Aria2Manager {

    private const val RPC_URL = "http://127.0.0.1:6800/jsonrpc"
    private val mmkv = MMKV.mmkvWithID("ACCOUNT", MMKV.MULTI_PROCESS_MODE)

    fun addUri(url: String, fileName: String) {
        try {
            val httpClient = OkHttpClient.Builder().build()

            val option = HashMap<String, Any>()
            option["out"] = fileName

            val map = HashMap<String, Any>()
            map["jsonrpc"] = "2.0"
            map["method"] = "aria2.addUri"
            map["id"] = "QXJpYU5nXzE2MTM4OTMxMjBfMC4wODg4ODM0MTIzNDc0Mzc4"
            map["params"] = arrayListOf(
                arrayListOf("$url&access_token=${mmkv?.decodeString("access_token")}"),
                option
            )
            val request = Request.Builder()
                .url(RPC_URL)
                .post(Gson().toJson(map).toRequestBody())
                .build()

            val response = httpClient.newCall(request).execute()

            response.body?.string()?.let { Log.d("download", it) }
        } catch (e: Exception) {
            e.message?.let { Log.e("aria2Manager", it) }
        }
    }


    @ExperimentalStdlibApi
    fun tellStatus() {
        try {
            val httpClient = OkHttpClient.Builder().build()
            val map = HashMap<String, Any>()
            map["jsonrpc"] = "2.0"
            map["method"] = "aria2.tellStopped"
            map["id"] = "QXJpYU5nXzE2MTM4OTMxMjBfMC4wODg4ODM0MTIzNDc0Mzc4"
            map["params"] = arrayListOf(
                -1,
                1000,
                arrayListOf("gid", "files", "totalLength", "completedLength")
            )
            val request = Request.Builder()
                .url(RPC_URL)
                .post(Gson().toJson(map).toRequestBody())
                .build()

            val response = httpClient.newCall(request).execute()

            response.body?.string()?.let {
                val gson = Gson()

                val fromJson: Aria2Response<List<TaskInfo>> = gson.fromJson(
                    it,
                    object : TypeToken<Aria2Response<List<TaskInfo>>>() {}.type
                )
                println(fromJson.result[0].completedLength)
                println(fromJson.result[0].totalLength)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}