package com.example.pan.model


data class GlobalStatResponse(
    val id: String,
    val jsonrpc: String,
    val result: GlobalStat,
)

data class GlobalStat(
    val downloadSpeed: String,
    val uploadSpeed: String,
    val numActive: String,
    val numStopped: String,
    val numStoppedTotal: String,
    val numWaiting: String,
)
