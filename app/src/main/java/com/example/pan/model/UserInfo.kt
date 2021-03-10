package com.example.pan.model

import com.example.pan.ui.Utils

data class UserInfo(
    val request_id: String,
    val errno: Int,
    val errmsg: String,
    val avatar_url: String,
    val baidu_name: String,
    val netdisk_name: String,
    val vip_type: Int,
    val uk: Int
) {
    var used: String = ""
        set(value) {
            field = Utils.formatBit(value)
        }
    var total: String = ""
        set(value) {
            field = Utils.formatBit(value)
        }
}


data class Quota(
    val errno: Int,
    val used: Long,
    val total: Long,
    val request_id: Long
)