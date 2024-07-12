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
                resume()
                return
            }
            stop()
            currentUrl = url
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                        AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build()
                )
                setDataSource(url)
                prepareAsync()
                setOnPreparedListener { mp ->
                        mp.start()
                    this@Player.isPlaying = true
                    // 在播放前检查是否已准备好并处于播放状态
                    if (mp.isPlaying) {
                        if (mediaPlayer?.isPlaying == true) {
                            val handler = Handler(Looper.getMainLooper())
                            handler.postDelayed(object : Runnable {
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
