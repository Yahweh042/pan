package com.example.pan.model


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
