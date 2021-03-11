package com.example.pan.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope


data class Aria2Response<out T>(
    val id: String = "",
    val jsonrpc: String = "",
    val result: T,
    val error: Aria2Error
)

suspend fun <T : Any> Aria2Response<T>.doError(errorBlock: (suspend CoroutineScope.(String) -> Unit)? = null): Aria2Response<T> {
    return coroutineScope {
        errorBlock?.invoke(this, this@doError.error.message)
        this@doError
    }
}

data class Aria2Error(
    val code: Int,
    val message: String
)

data class GlobalStat(
    val downloadSpeed: String,
    val uploadSpeed: String,
    val numActive: String,
    val numStopped: String,
    val numStoppedTotal: String,
    val numWaiting: String,
)


data class Aria2TaskInfo(
    val bitfield: String,
    val gid: String,
    val connections: String,
    val status: String,
    val errorCode: String,
    val errorMessage: String,
    val downloadSpeed: String,
    val totalLength: String,
    val completedLength: String,
    val dir: String,
    val files: List<Aria2File>
)

data class Aria2File(
    val gid: String

)