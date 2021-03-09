package com.example.pan.aria2

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.pan.MainActivity
import com.example.pan.R
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import kotlin.concurrent.thread

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

    override fun onBind(intent: Intent): IBinder = mBinder

    override fun onCreate() {
        super.onCreate()
        val notification = buildNotification()

        fileAria2c = File(applicationInfo.nativeLibraryDir, "libaria2c.so")
        fileConf = File(filesDir, "aria2.conf")
        fileConf.delete()
        if (!fileConf.exists()) {
            try {
                val confFile = assets.open("aria2.conf")
                fileConf.writeBytes(confFile.readBytes())
                Runtime.getRuntime().exec("chmod 777 " + fileConf.absoluteFile)
            } catch (e: Exception) {
                Log.e(TAG, "初始化aria2c文件失败:", e)
            }
        }
        Log.e(
            "path",
            fileAria2c.canonicalPath + " --conf-path=${fileConf.absoluteFile} --check-certificate=false"
        )
        try {
            val exec = Runtime.getRuntime()
                .exec(fileAria2c.canonicalPath + " --conf-path=${fileConf.absoluteFile} --check-certificate=false")
            thread {
                val reader = BufferedReader(InputStreamReader(exec.inputStream))
                var line: String
                do {
                    line = reader.readLine()
                    if (line == null) {
                        break
                    }
                    Log.e("process", line)
                } while (true)
            }
            startForeground(1, notification)
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "初始化aria2c服务失败:${e.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun buildNotification(): Notification {
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
        return NotificationCompat.Builder(this, "com.example.pan")
            .setContentTitle("Aria2c")
            .setContentText("Aria2c服务正在运行中")
            .setContentIntent(pi)
            .setSmallIcon(R.drawable.ic_cloud_download_black_24dp)
            .build()
    }

    fun storeAllCertificates() {

    }
}