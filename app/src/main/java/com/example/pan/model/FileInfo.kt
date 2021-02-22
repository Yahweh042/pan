package com.example.pan.model

import com.google.gson.annotations.SerializedName

data class FileInfo(
    @SerializedName("server_filename") val server_filename: String,
    @SerializedName("category") val category: Int,
    @SerializedName("unlist") val unlist: Int,
    @SerializedName("fs_id") val fs_id : Long,
    @SerializedName("dir_empty") val dir_empty: Int,
    @SerializedName("server_mtime") val server_mtime: Long,
    @SerializedName("server_atime") val server_atime: Long,
    @SerializedName("server_ctime") val server_ctime: Long,
    @SerializedName("wpfile") val wpfile: Int,
    @SerializedName("isdir") val isdir: Int,
    @SerializedName("local_mtime") val local_mtime: Long,
    @SerializedName("size") val size: Long,
    @SerializedName("share") val share: Int,
    @SerializedName("path") val path: String,
    @SerializedName("local_ctime") val local_ctime: Long,
    @SerializedName("empty") val empty: Int,
    @SerializedName("oper_id") val oper_id: Int,
    @SerializedName("pl") val pl: Int
)
