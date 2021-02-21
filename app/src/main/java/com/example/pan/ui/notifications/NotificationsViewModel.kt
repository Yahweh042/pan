package com.example.pan.ui.notifications

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pan.model.GlobalStat
import com.example.pan.model.GlobalStatResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.concurrent.thread

class NotificationsViewModel : ViewModel() {

    val globalStat = MutableLiveData<GlobalStat>()

    fun getGlobalStat() {

        viewModelScope.launch {
            thread {
                while (true) {
                    val build = OkHttpClient.Builder().build()

                    val request = Request.Builder()
                        .url("http://127.0.0.1:6800/jsonrpc")
                        .post(
                            "{\"jsonrpc\":\"2.0\",\"method\":\"aria2.getGlobalStat\",\"id\":\"QXJpYU5nXzE2MTM4OTMxMjBfMC4wODg4ODM0MTIzNDc0Mzc4\"}".toRequestBody(
                                "".toMediaTypeOrNull()
                            )
                        )
                        .build()

                    val execute = build.newCall(request).execute()

                    val fromJson =
                        Gson().fromJson(execute.body?.string(), GlobalStatResponse::class.java)

                    globalStat.postValue(fromJson.result)

                    Thread.sleep(1000)
                }
            }
        }

    }

}