package com.example.pan.http

import com.example.pan.model.*
import okhttp3.RequestBody
import retrofit2.http.*


interface ApiService {

    /**
     * 获取用户信息
     */
    @GET("/rest/2.0/xpan/nas?method=uinfo")
    suspend fun getUserInfo(): UserInfo

    /**
     * 获取网盘容量
     */
    @GET("/api/quota")
    suspend fun quota(): Quota

    /**
     * 获取文件列表
     */
    @GET("/rest/2.0/xpan/file?method=list")
    suspend fun list(
        @Query("dir") dir: String,
        @Query("order") order: String,
        @Query("desc") desc: String,
        @Query("start") start: Int,
        @Query("limit") num: Int,
    ): ResponseData<FileInfo>

    /**
     * 获取文件信息
     */
    @GET("/rest/2.0/xpan/multimedia?method=filemetas&dlink=1")
    suspend fun filemetas(
        @Query("fsids") fsids: String
    ): ResponseData<FileMeta>

    /**
     * 获取已下载完成列表
     */
    @Headers("baseUrl: aria2")
    @POST("/")
    suspend fun tellStopped(@Body body: RequestBody): Aria2Response<List<Aria2TaskInfo>>

    /**
     * 获取正在下载完成列表
     */
    @Headers("baseUrl: aria2")
    @POST("/")
    suspend fun tellActive(@Body body: RequestBody): Aria2Response<List<Aria2TaskInfo>>

    /**
     * 添加Url
     */
    @Headers("baseUrl: aria2")
    @POST("/")
    suspend fun addUri(@Body body: RequestBody): Aria2Response<String>

}