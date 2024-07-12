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
    var mService: MusicService? = null

    private val mConn: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            componentName: ComponentName, iBinder: IBinder
        ) {
            val binder = iBinder as MusicService.MusicServiceBinder
            mService = binder.service
        }

        override fun onServiceDisconnected(
            componentName: ComponentName
        ) {
            mService = null
        }
    }
    // 绑定服务
    fun bindToMusicService(context: Context) {
        val intent = Intent(
            context,
            MusicService::class.java
        )
        context.bindService(intent, mConn, Context.BIND_AUTO_CREATE)
    }

    // 解绑服务
    fun unbindToMusicService(context: Context) {
        context.unbindService(mConn)
    }


}