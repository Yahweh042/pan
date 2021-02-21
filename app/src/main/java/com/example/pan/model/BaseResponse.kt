package com.example.pan.model

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("errno") val errno: Int,
    @SerializedName("guid_info") val guid_info: String,
    @SerializedName("list") val list: List<T>,
    @SerializedName("request_id") val request_id: Long,
    @SerializedName("guid") val guid: Int
)
