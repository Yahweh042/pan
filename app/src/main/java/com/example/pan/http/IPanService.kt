package com.example.pan.http

import com.example.pan.model.BaseResponse
import com.example.pan.model.FileInfo
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
    ): BaseResponse<FileInfo>

}