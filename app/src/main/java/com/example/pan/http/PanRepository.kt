package com.example.pan.http

import android.util.Log
import com.example.pan.model.ResponseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import retrofit2.http.Query
import javax.inject.Inject

class PanRepository @Inject constructor(
    val service: IPanService
) {

    @ExperimentalCoroutinesApi
    fun getUserInfo() = flow {
        val userInfo = service.getUserInfo()
        val quota = service.quota()
        userInfo.used = quota.used.toString()
        userInfo.total = quota.total.toString()
        userInfo.percent = quota.percent
        emit(userInfo)
    }.catch {
        it.message?.let { it1 -> Log.e("http", it1) }
    }.flowOn(Dispatchers.IO)

    @ExperimentalCoroutinesApi
    fun list(
        @Query("dir") dir: String,
        @Query("order") order: String,
        @Query("desc") desc: String,
        @Query("start") start: Int,
        @Query("limit") num: Int
    ) = flow {
        val list = service.list(dir, order, desc, start, num)
        emit(list)
    }.onStart {

    }.catch {
        emit(ResponseData(errno = -1, guid_info = it.message))
    }.flowOn(Dispatchers.IO)


    @ExperimentalCoroutinesApi
    fun download(fs_id: Long) = flow {
        val list = service.filemetas(arrayListOf(fs_id).toString())
        if (list.list.isNotEmpty()) {
            emit(list.list[0])
        } else {
            emit(null)
        }
    }.catch {
        it.message?.let { it1 -> Log.e("http", it1) }
    }.flowOn(Dispatchers.IO)
}