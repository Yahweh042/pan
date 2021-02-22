package com.example.pan.aria2

import android.util.Log


class Aria2Thread(
    private val bin: String,
    private val params: String
) : Runnable {

    companion object {
        const val TAG = "Aria2Thread"
    }

    override fun run() {
        try {
            Runtime.getRuntime().exec("$bin $params")
        } catch (ex: Exception) {
            Log.e(TAG, "aria2c启动失败:", ex)
        }
    }
}