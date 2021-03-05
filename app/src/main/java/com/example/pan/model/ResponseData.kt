package com.example.pan.model

import com.google.gson.annotations.SerializedName

data class ResponseData<T>(
    @SerializedName("errno") val errno: Int = 0,
    @SerializedName("guid_info") val guid_info: String? = "",
    @SerializedName("list") val list: List<T> = arrayListOf(),
    @SerializedName("request_id") val request_id: Long = 0L,
    @SerializedName("guid") val guid: Int = 0
)