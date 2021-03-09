package com.example.pan.model

import com.example.pan.ui.Utils


data class Aria2Response<T>(
    val id: String,
    val jsonrpc: String,
    val result: T,
)

data class GlobalStat(
    val downloadSpeed: String,
    val uploadSpeed: String,
    val numActive: String,
    val numStopped: String,
    val numStoppedTotal: String,
    val numWaiting: String,
)


data class TaskInfo(
    val gid: String,

    ) {
    val totalLength: String = "0"
        get() {
            return Utils.formatBit(field)
        }
    val completedLength: String = "0"
        get() {
            return Utils.formatBit(field)
        }
}