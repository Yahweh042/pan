package com.example.pan.ui.notifications

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pan.aria2.Aria2Repository
import com.example.pan.model.DownloadTaskInfo
import com.example.pan.model.GlobalStat
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.thread

class NotificationsViewModel @ViewModelInject constructor(
    private val aria2Repository: Aria2Repository
) : ViewModel() {

    val globalStat = MutableLiveData<GlobalStat>()
    val stoppedTask = MutableLiveData<List<DownloadTaskInfo>>()
    val activeTask = MutableLiveData<List<DownloadTaskInfo>>()

    @ExperimentalCoroutinesApi
    fun tellActive() {
        viewModelScope.launch(Dispatchers.IO) {
            aria2Repository.tellActive().catch {
                it.message?.let { it1 -> Log.d("tellActive", it1) }
            }.collect {
                activeTask.postValue(ArrayList(it))
                Log.d("tellActive", Gson().toJson(it))
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun tellStopped() {
        viewModelScope.launch(Dispatchers.IO) {
            aria2Repository.tellStopped().catch {
                it.message?.let { it1 -> Log.d("tellActive", it1) }
            }.collect {
                stoppedTask.postValue(ArrayList(it))
                Log.d("tellStopped", Gson().toJson(it))
            }
        }
    }

    fun getGlobalStat() {

        viewModelScope.launch {
            thread {
                while (true) {
//                    val build = OkHttpClient.Builder().build()
//
//                    val request = Request.Builder()
//                        .url("http://127.0.0.1:6800/jsonrpc")
//                        .post(
//                            "{\"jsonrpc\":\"2.0\",\"method\":\"aria2.getGlobalStat\",\"id\":\"QXJpYU5nXzE2MTM4OTMxMjBfMC4wODg4ODM0MTIzNDc0Mzc4\"}".toRequestBody(
//                                "".toMediaTypeOrNull()
//                            )
//                        )
//                        .build()
//
//                    val execute = build.newCall(request).execute()
//
//                    val fromJson = Gson().fromJson(execute.body?.string(), GlobalStatResponse::class.java)
//
//                    globalStat.postValue(fromJson.result)
//
//                    Thread.sleep(1000)
                }
            }
        }

    }

}