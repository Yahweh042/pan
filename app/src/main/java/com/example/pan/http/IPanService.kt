package com.example.pan.http

import com.example.pan.model.FileInfo
import com.example.pan.model.ResponseData
import com.example.pan.model.TemplateVariable
import retrofit2.http.GET
import retrofit2.http.Query


interface IPanService {

    @GET("/api/list")
    suspend fun list(
        @Query("order") order: String,
        @Query("desc") desc: String,
        @Query("showempty") showEmpty: String,
        @Query("page") page: Int,
        @Query("num") num: Int,
        @Query("dir") dir: String,
    ): ResponseData<FileInfo>


    @GET("/api/gettemplatevariable")
    suspend fun getTemplateVariable(
        @Query("fields") fields: List<String>
    ): TemplateVariable

    @GET("/api/download")
    suspend fun download(
        @Query("fidlist") fields: List<Long>,
        @Query("sign") sign: String,
        @Query("type") type: String,
        @Query("vip") vip: Int
    ): TemplateVariable

}