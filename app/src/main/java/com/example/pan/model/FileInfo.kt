package com.example.pan.model

import com.google.gson.annotations.SerializedName

data class FileInfo(
    @SerializedName("server_filename") val server_filename: String,
    @SerializedName("category") val category: Int,
    @SerializedName("unlist") val unlist: Int,
    @SerializedName("fs_id") val fs_id : Long,
    @SerializedName("dir_empty") val dir_empty: Int,
    @SerializedName("server_mtime") val server_mtime: Int,
    @SerializedName("server_atime") val server_atime: Int,
    @SerializedName("server_ctime") val server_ctime: Int,
    @SerializedName("wpfile") val wpfile: Int,
    @SerializedName("isdir") val isdir: Int,
    @SerializedName("local_mtime") val local_mtime: Int,
    @SerializedName("size") val size: Long,
    @SerializedName("share") val share: Int,
    @SerializedName("path") val path: String,
    @SerializedName("local_ctime") val local_ctime: Int,
    @SerializedName("empty") val empty: Int,
    @SerializedName("oper_id") val oper_id: Int,
    @SerializedName("pl") val pl: Int
)
