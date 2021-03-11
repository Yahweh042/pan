package com.example.pan.aria2

import com.example.pan.data.Account
import com.example.pan.http.ApiService
import com.example.pan.model.DownloadTaskInfo
import com.example.pan.ui.Utils
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ScheduledThreadPoolExecutor
import javax.inject.Inject

class Aria2Repository @Inject constructor(
    private var apiService: ApiService
) {

    val cache: MutableMap<String, DownloadTaskInfo> = ConcurrentHashMap()
    private val mTimer = ScheduledThreadPoolExecutor(1)

    @ExperimentalCoroutinesApi
    fun addUri(url: String, fileName: String) = flow {
        val option = HashMap<String, Any>()
        option["out"] = fileName
        val response = apiService.addUri(
            requestBody(
                "aria2.addUri", arrayListOf(
                    arrayListOf("$url&access_token=${Account.getAccessToken()}"),
                    option
                )
            )
        )
        val gid = response.result
        cache[gid] = DownloadTaskInfo(gid, fileName)
        emit(gid)
    }.flowOn(Dispatchers.IO)


    @ExperimentalCoroutinesApi
    fun tellStopped() = flow {
        while (true) {
            val response =
                apiService.tellStopped(requestBody("aria2.tellStopped", arrayListOf(-1, 1000)))
            val result = arrayListOf<DownloadTaskInfo>()
            for (item in response.result) {
                cache[item.gid]?.let {
                    it.connections = item.connections
                    it.totalLength = Utils.formatBit(item.totalLength)
                    it.completeLength = Utils.formatBit(item.completedLength)
                    it.downloadSpeed = Utils.formatBit(item.downloadSpeed) + "/s"
                    it.process =
                        (item.completedLength.toDouble() / item.completedLength.toDouble() * 100).toInt()
                    result.add(it)
                }
            }
            emit(result)
            delay(1000)
        }
    }.flowOn(Dispatchers.IO)

    @ExperimentalCoroutinesApi
    fun tellActive() = flow {
        while (true) {
            val response = apiService.tellActive(requestBody("aria2.tellActive", arrayListOf()))
            val result = arrayListOf<DownloadTaskInfo>()
            for (item in response.result) {
                cache[item.gid]?.let {
                    it.connections = item.connections
                    it.totalLength = Utils.formatBit(item.totalLength)
                    it.completeLength = Utils.formatBit(item.completedLength)
                    it.downloadSpeed = Utils.formatBit(item.downloadSpeed) + "/s"
                    it.process =
                        (item.completedLength.toDouble() / item.totalLength.toDouble() * 100).toInt()
                    result.add(it)
                }
            }
            emit(result)
            delay(1000)
        }
    }.flowOn(Dispatchers.IO)


    fun requestBody(method: String, params: ArrayList<Any>): RequestBody {
        val map = HashMap<String, Any>()
        map["jsonrpc"] = "2.0"
        map["method"] = method
        map["id"] = "QXJpYU5nXzE2MTM4OTMxMjBfMC4wODg4ODM0MTIzNDc0Mzc4"
        map["params"] = params
        return Gson().toJson(map).toRequestBody()
    }


}