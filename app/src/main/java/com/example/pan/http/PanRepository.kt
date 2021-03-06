package com.example.pan.http

import com.example.pan.model.ResponseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class PanRepository @Inject constructor(
    val service: IPanService
) {

    @ExperimentalCoroutinesApi
    fun list(
        order: String,
        desc: String,
        showEmpty: String,
        page: Int,
        num: Int,
        dir: String
    ) = flow {
        val list = service.list(order, desc, showEmpty, page, num, dir)
        emit(list)
    }.onStart {

    }.catch {
        emit(ResponseData(errno = -1, guid_info = it.message))
    }.flowOn(Dispatchers.IO)




    fun download() = flow {
        val templateVariable = service.getTemplateVariable(arrayListOf("sign1", "sign2", "sign3", "timestamp"))
        emit(templateVariable)
    }.catch {
    }.flowOn(Dispatchers.IO)
}