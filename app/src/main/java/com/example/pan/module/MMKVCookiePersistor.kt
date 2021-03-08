package com.example.pan.module

import com.franmontiel.persistentcookiejar.persistence.CookiePersistor
import com.franmontiel.persistentcookiejar.persistence.SerializableCookie
import com.tencent.mmkv.MMKV
import okhttp3.Cookie
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

class MMKVCookiePersistor : CookiePersistor {

    private val mmkv = MMKV.mmkvWithID("CookieStore", MMKV.MULTI_PROCESS_MODE)

    override fun loadAll(): MutableList<Cookie> {
        val cookies: MutableList<Cookie> = arrayListOf()
        mmkv?.allKeys()?.forEach {
            cookies.add(SerializableCookie().decode(mmkv.decodeString(it)))
        }
        return cookies
    }

    fun saveAll(cookies: String) {
        for (item in cookies.split(";")) {
            "https://pan.baidu.com/".toHttpUrlOrNull()?.let { httpUrl ->
                Cookie.parse(httpUrl, item)?.let {
                    mmkv?.encode(createCookieKey(it), SerializableCookie().encode(it))
                }
            }
        }
    }

    override fun saveAll(cookies: MutableCollection<Cookie>?) {
        cookies?.forEach {
            mmkv?.encode(createCookieKey(it), SerializableCookie().encode(it))
        }
    }

    override fun removeAll(cookies: MutableCollection<Cookie>?) {
        cookies?.forEach {
            mmkv?.removeValueForKey(createCookieKey(it))
        }
    }

    override fun clear() {
        mmkv?.clearAll()
    }

    private fun createCookieKey(cookie: Cookie): String =
        "${if (cookie.secure) "https" else "http"}://${cookie.domain}${cookie.path}|${cookie.name}"

}