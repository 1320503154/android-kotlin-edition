package com.example.ggmusic

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.ggmusic.Audio.AudioFile
import com.example.ggmusic.Audio.AudioList
import com.example.ggmusic.Audio.getAudioFiles
import com.example.ggmusic.Player.Player
import com.example.ggmusic.Player.PlayerViewModel

class MainActivity : ComponentActivity() {
    // 请求码，用于在回调中识别是哪个权限请求的结果
    private val PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 1
    private var audioFiles = mutableStateOf<List<AudioFile>>(listOf())
    private var player: Player? by mutableStateOf(null)
    private val playerViewModel: PlayerViewModel by viewModels()

    override fun onStart() {
        super.onStart()
        playerViewModel.bindToMusicService(baseContext)
    }

    override fun onStop() {
        //这个 Context 对象包含了关于 MainActivity 的环境信息。 通过这个 Context 对象，我们可以访问 Activity 的资源和类，以及调用 Activity 的方法。
        playerViewModel.unbindToMusicService(baseContext)
        super.onStop()
    }

    companion object {
        // 用于传递音频文件信息的 Intent Extra 键,在Notification中使用
        const val URL = "URL"//创建一个常量,类似于java中的静态常量
        const val TITLE = "TITLE"
        const val ARTIST = "ARTIST"
    }
    override fun onDestroy() {
        player?.stop()
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_MEDIA_AUDIO),
                PERMISSION_REQUEST_READ_EXTERNAL_STORAGE
            )
        } else {
            // 权限已授予，可以直接获取音频文件列表
            audioFiles.value = getAudioFiles(this)
        }
        //申请READ_EXTERNAL_STORAGE
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                0)
        }else{
            audioFiles.value = getAudioFiles(this)
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.FOREGROUND_SERVICE), 2)
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK), 3)
        }
        setContent {
            // 显示音频文件列表
            AudioList(audioFiles.value, playerViewModel)
        }
    }
}