package com.example.ggmusic.Player

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import com.example.ggmusic.MusicService

class PlayerViewModel:ViewModel() {
    @SuppressLint("StaticFieldLeak")
    var mService: MusicService? = null//存储音乐服务的实例

    private val mConn: ServiceConnection = object : ServiceConnection {
        //处理服务链接和断开连接的情况
        override fun onServiceConnected(
            componentName: ComponentName, iBinder: IBinder
        ) {
            //通过 iBinder 获取到 MusicService 的实例并赋值给 mService
            val binder = iBinder as MusicService.MusicServiceBinder
            mService = binder.service
        }
        //当服务断开连接时，将 mService 设置为 null。
        override fun onServiceDisconnected(
            componentName: ComponentName
        ) {
            mService = null
        }
    }
    // 绑定服务
    fun bindToMusicService(context: Context) {
        //在 bindToMusicService 方法中，创建了一个 Intent，并通过 context.bindService 方法将 mConn 和音乐服务绑定
        val intent = Intent(
            context,
            MusicService::class.java
        )
        context.bindService(intent, mConn, Context.BIND_AUTO_CREATE)//绑定服务
    }

    // 解绑服务
    fun unbindToMusicService(context: Context) {
        context.unbindService(mConn)
    }


}