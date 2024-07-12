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
    private var player: Player? by mutableStateOf(null)//播放器实例
    private val mBinder: IBinder = MusicServiceBinder()//Binder对象,可以在Activity中获取Service实例
    inner class MusicServiceBinder : Binder() {
        val service: MusicService
            get() = this@MusicService
    }

    @RequiresApi(Build.VERSION_CODES.R)//Android 11及以上版本
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {//服务启动时调用

        val title = intent?.getStringExtra(MainActivity.TITLE)//获取音乐标题
        val artist = intent?.getStringExtra(MainActivity.ARTIST)//获取音乐作者
        val channelId = "channel_myself_01"//通知渠道ID
        val context = baseContext//获取环境上下文
        val notificationIntent = Intent(context, MainActivity::class.java)//通知点击后跳转的Activity
        notificationIntent.flags =//设置Intent的标志
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP // 确保正确启动并清除栈顶任务
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {//Android 12及以上版本
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//Android 8.0及以上版本
            val name = "My App's Notification Channel"
            val descriptionText = "Music Player Notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            // 获取系统服务并注册通知渠道
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }


        val notificationId = 1234 // 任意唯一的ID用于标识这个通知
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.baseline_album_24) // 设置通知小图标
            .setContentTitle(title)
            .setContentText(artist)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true) // 用户点击后自动清除通知
            .setContentIntent(pendingIntent)
            .build()

        try {
            player?.play(intent?.getStringExtra(MainActivity.URL)!!)//播放音乐
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
