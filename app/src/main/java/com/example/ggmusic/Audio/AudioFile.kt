package com.example.ggmusic.Audio

data class AudioFile(
    val id: Long = -1,//音频文件的id
    val title: String = "默认标题",//音频文件的标题
    val artist: String = "默认作者",//音频文件的艺术家
    val filePath: String = "",//音频文件的路径
    val duration: Int = 0//音频文件的时长
)
