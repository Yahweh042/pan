package com.example.pan.data

import android.text.TextUtils
import com.tencent.mmkv.MMKV

object Account {

    private val mmkv = MMKV.mmkvWithID("ACCOUNT", MMKV.MULTI_PROCESS_MODE)
    private var accessToken: String? = null

    fun setAccessToken(token: String) {
        accessToken = token
        mmkv?.encode("access_token", token)
    }

    fun getAccessToken(): String {
        return accessToken.let {
            mmkv?.decodeString("access_token", "")!!
        }
    }

    fun isOnline(): Boolean = !TextUtils.isEmpty(getAccessToken())

}