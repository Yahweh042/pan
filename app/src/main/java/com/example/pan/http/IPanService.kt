package com.example.pan.http

import com.example.pan.model.FileInfo
import com.example.pan.model.FileMeta
import com.example.pan.model.ResponseData
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

    @GET("/rest/2.0/xpan/file?method=list")
    suspend fun list(
        @Query("dir") dir: String,
        @Query("order") order: String,
        @Query("desc") desc: String,
        @Query("start") start: Int,
        @Query("limit") num: Int,
    ): ResponseData<FileInfo>

    @GET("/rest/2.0/xpan/multimedia?method=filemetas&dlink=1")
    suspend fun filemetas(
        @Query("fsids") fsids: String
    ): ResponseData<FileMeta>
}