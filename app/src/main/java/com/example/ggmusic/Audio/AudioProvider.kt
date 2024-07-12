package com.example.ggmusic.Audio

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore

fun getAudioFiles(context: Context): List<AudioFile> {
    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DATA, // 文件路径
        MediaStore.Audio.Media.DURATION
    )

    val selection =
        "${MediaStore.Audio.Media.IS_MUSIC} != 0 AND ${MediaStore.Audio.Media.ARTIST} NOT LIKE '%<unknown>%'"
    val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"

    val contentResolver = context.contentResolver
    val cursor: Cursor? = contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        projection,
        selection,
        null,
        sortOrder
    )

    val audioFiles = mutableListOf<AudioFile>()
    if (cursor != null) {
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
            val artist =
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
            val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
            val duration =
                cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))

            audioFiles.add(AudioFile(id, title, artist, path, duration))
        }
        cursor.close()
    }

    return audioFiles
}
