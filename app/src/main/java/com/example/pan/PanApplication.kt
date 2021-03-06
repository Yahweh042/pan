package com.example.pan

import android.app.Application
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PanApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(applicationContext)
    }

}