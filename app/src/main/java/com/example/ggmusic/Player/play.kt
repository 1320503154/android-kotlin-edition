package com.example.ggmusic.Player;

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class Player {
    private var mediaPlayer: MediaPlayer? = null
    private var currentUrl: String? = null

    private var isPlaying by mutableStateOf(false)
    var currentPosition by mutableStateOf(0)


    fun getIsPlaying(): Boolean {
        return isPlaying
    }


    fun play(url: String) {
        if (url != "") {
            if (currentUrl == url) {
                resume()//如果当前播放的音乐和要播放的音乐是同一首，那么直接调用 resume 方法,恢复播放
                return
            }
            stop()
            currentUrl = url
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(//设置音频属性
                        AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build()//设置音频类型为音乐
                )
                setDataSource(url)//设置数据源
                prepareAsync()//异步准备
                setOnPreparedListener { mp ->
                        mp.start()//准备好后开始播放
                    this@Player.isPlaying = true//设置播放状态为 true
                    // 在播放前检查是否已准备好并处于播放状态
                    if (mp.isPlaying) {
                        if (mediaPlayer?.isPlaying == true) {
                            val handler = Handler(Looper.getMainLooper())
                            handler.postDelayed(object : Runnable {
                                //总体实现思路是通过 Handler 定时获取当前播放位置并更新进度(每秒更新一次)
                                override fun run() {
                                    // 在播放前检查是否已准备好并处于播放状态
                                    if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                                        // 获取当前播放位置
                                        this@Player.currentPosition =
                                        mediaPlayer!!.currentPosition

                                    }
                                    handler.postDelayed(this, 1000) // 每秒更新一次进度
                                }
                            }, 0)

                        }
                    }
                }
                setOnCompletionListener {
                    this@Player.isPlaying = false
                }
            }
        }
    }

    fun pause() {
        mediaPlayer?.pause()
        isPlaying = false
    }

    fun resume() {
        mediaPlayer?.start()
        isPlaying = true
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false

    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }
}
