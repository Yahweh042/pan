package com.example.pan.model


data class DownloadTaskInfo(
    val gid: String,
    val fileName: String,
    var totalLength: String = "",
    var completeLength: String = "",
    var process: Int = 0,
    var connections: String = "0",
    var downloadSpeed: String = ""
)
