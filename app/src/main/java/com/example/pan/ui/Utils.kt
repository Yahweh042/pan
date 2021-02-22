package com.example.pan.ui

import android.os.Build
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log10
import kotlin.math.pow


object Utils {


    fun is64bit(): Boolean {
        return Arrays.binarySearch(Build.SUPPORTED_ABIS, "arm64-v8a") >= 0
    }

    fun formatDate(time: Long): String {
        val simpleDateFormat = SimpleDateFormat.getDateTimeInstance()
        return simpleDateFormat.format(Date(time * 1000))
    }

    fun formatBit(input: Long): String {
        if (input <= 0) {
            return "0B"
        }
        val units = arrayListOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(input.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(input / 1024.0.pow(digitGroups.toDouble()))
            .toString() + " " + units[digitGroups]

    }

}