package com.example.pan.http

import com.example.pan.model.*
import retrofit2.http.GET
import retrofit2.http.Query


interface IPanService {

    @GET("/rest/2.0/xpan/nas?method=uinfo")
    suspend fun getUserInfo(): UserInfo

    @GET("/api/quota")
    suspend fun quota(): Quota

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