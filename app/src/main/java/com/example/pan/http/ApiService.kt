package com.example.pan.http

import com.example.pan.model.*
import okhttp3.RequestBody
import retrofit2.http.*


interface ApiService {

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

    @Headers("baseUrl: aria2")
    @POST("/")
    suspend fun tellStopped(@Body body: RequestBody): Aria2Response<List<Aria2TaskInfo>>

    @Headers("baseUrl: aria2")
    @POST("/")
    suspend fun tellActive(@Body body: RequestBody): Aria2Response<List<Aria2TaskInfo>>

    @Headers("baseUrl: aria2")
    @POST("/")
    suspend fun addUri(@Body body: RequestBody): Aria2Response<String>

}