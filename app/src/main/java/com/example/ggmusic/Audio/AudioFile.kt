package com.example.ggmusic.Audio

data class AudioFile(
    val id: Long = -1,
    val title: String = "------",
    val artist: String = "---",
    val filePath: String = "",
    val duration: Int = 0
)
