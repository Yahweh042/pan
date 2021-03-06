package com.example.pan.aria2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.pan.MainActivity
import com.example.pan.R
import com.example.pan.ui.Utils
import java.io.File

class Aria2Service : Service() {

    companion object {
        const val TAG = "Aria2Service"
    }

    private val mBinder = Aria2Binder()

    inner class Aria2Binder : Binder() {

        fun getService(): Aria2Service = this@Aria2Service

    }

    private lateinit var fileAria2c: File
    private lateinit var fileConf: File
    lateinit var thread: Aria2Thread

    override fun onBind(intent: Intent): IBinder = mBinder

    override fun onCreate() {
        super.onCreate()
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "com.example.pan",
                "下载服务常驻通知",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }
        val intent = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, 0)
        val notification = NotificationCompat.Builder(this, "com.example.pan")
            .setContentTitle("Aria2c")
            .setContentText("Aria2c服务正在运行中")
            .setContentIntent(pi)
            .setSmallIcon(R.drawable.baseline_cloud_download_black_24dp)
            .build()

        fileAria2c = File(applicationInfo.nativeLibraryDir, "libaria2c.so")
        fileConf = File(filesDir, "aria2.conf")
        if (!fileConf.exists()) {
            try {
                val confFile = assets.open("aria2.conf")
                fileConf.writeBytes(confFile.readBytes())
                Runtime.getRuntime().exec("chmod 777 " + fileConf.absoluteFile)
            } catch (e: Exception) {
                Log.e(TAG, "初始化aria2c文件失败:", e)
            }
        }
        Log.e("path",fileAria2c.canonicalPath + " --conf-path=${fileConf.absoluteFile}")
        try {
            Runtime.getRuntime().exec(fileAria2c.canonicalPath + " --conf-path=${fileConf.absoluteFile}")
            startForeground(1, notification)
        } catch (e: Exception) {
            e.message?.let { Log.e("aria2c start", it) }
            Toast.makeText(applicationContext, "初始化aria2c服务失败:${e.message}", Toast.LENGTH_SHORT).show()
        }
//        thread = Aria2Thread(fileAria2c.absolutePath, "--conf-path=${fileConf.absoluteFile}")
//        Thread(thread).start()
    }


}