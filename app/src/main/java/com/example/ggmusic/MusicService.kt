package com.example.ggmusic

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import com.example.ggmusic.Player.Player

class MusicService : Service() {
    private var player: Player? by mutableStateOf(null)
    private val mBinder: IBinder = MusicServiceBinder()
    inner class MusicServiceBinder : Binder() {
        val service: MusicService
            get() = this@MusicService
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val title = intent?.getStringExtra(MainActivity.TITLE)
        val artist = intent?.getStringExtra(MainActivity.ARTIST)
        val channelId = "my_channel_01"
        val context = baseContext
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP // 确保正确启动并清除栈顶任务
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                this,
                0, // requestCode
                notificationIntent,
                flag // 使用上述flag变量
            )

        // 创建通知渠道（对于Android Oreo及以上版本）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Channel"
            val descriptionText = "This is my app's notification channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            // 获取系统服务并注册通知渠道
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }


        val notificationId = 12434 // 任意唯一的ID用于标识这个通知
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.baseline_album_24) // 设置通知小图标
            .setContentTitle(title)
            .setContentText(artist)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true) // 用户点击后自动清除通知
            .setContentIntent(pendingIntent)
            .build()

        try {
            player?.play(intent?.getStringExtra(MainActivity.URL)!!)
            startForeground(notificationId, notification, FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)

        } catch (e: Exception) {
            Log.e(TAG, "onStartCommand: $e")
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        player = Player()
    }

    override fun onDestroy() {
        player?.stop()
        super.onDestroy()
    }


    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }
    fun pause() {
        if (player != null && player!!.getIsPlaying()) {
            player!!.pause()
        }
    }
    fun resume() {
        player?.resume()
    }

    fun getCurrentPosition(): Int {

        return player?.currentPosition!!
    }

    fun isPlaying(): Boolean {
        return player?.getIsPlaying()!!
    }

    fun seekTo(position: Int) {
        player?.seekTo(position)
    }

}
