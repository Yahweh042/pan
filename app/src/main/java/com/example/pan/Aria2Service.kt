package com.example.pan

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import java.io.File

class Aria2Service : Service() {

    companion object {
        const val TAG = "Aria2Service"
    }

    private val mBinder = Aria2Binder()

    private lateinit var aria2c: File
    private lateinit var conf: File
    lateinit var thread: Aria2Thread

    override fun onBind(intent: Intent): IBinder = mBinder

    override fun onCreate() {
        super.onCreate()
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("com.example.pan", "下载服务常驻通知", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        val intent = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, 0)
        val notification = NotificationCompat.Builder(this, "com.example.pan")
            .setContentTitle("pan")
            .setContentText("下载服务正在运行")
            .setContentIntent(pi)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .build()
        startForeground(1, notification)

        aria2c = File(filesDir, "aria2c")
        conf = File(filesDir, "aria2.conf")
        if (!aria2c.exists() || !conf.exists()) {
            try {
                aria2c.delete()
                val aria2cFile = assets.open("aria2c")
                aria2c.writeBytes(aria2cFile.readBytes())
                Runtime.getRuntime().exec("chmod 777 " + aria2c.absolutePath)
                conf.delete()
                val confFile = assets.open("aria2.conf")
                conf.writeBytes(confFile.readBytes())
                Runtime.getRuntime().exec("chmod 777 " + conf.absolutePath)
            } catch (e: Exception) {
                Log.e(TAG, "初始化aria2c文件失败:", e)
            }
        }
        thread = Aria2Thread(aria2c.absolutePath, "--conf-path=${conf.absolutePath}")
        Thread(thread).start()
    }

    class Aria2Binder : Binder() {
        fun startAria2() {
        }

        fun stopAria2() {

        }
    }
}