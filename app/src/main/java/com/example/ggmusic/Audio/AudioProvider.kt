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
    //我们只想要设备中,那些是音乐并且艺术家不是未知的音频文件
    val selection =
        "${MediaStore.Audio.Media.IS_MUSIC} != 0 AND ${MediaStore.Audio.Media.ARTIST} NOT LIKE '%<unknown>%'"
    val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"//按照标题的升序排序

    val contentResolver = context.contentResolver//从媒体库中查询音频文件
    //将查询结果存储在 cursor,cursor是一个指向查询结果的指针,可以通过它来访问查询结果
    val cursor: Cursor? = contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        projection,
        selection,
        null,
        sortOrder
    )

    val audioFiles = mutableListOf<AudioFile>()//创建一个空的音频文件列表
    if (cursor != null) {//如果cursor不为空
        while (cursor.moveToNext()) {//遍历cursor中的每一行,cursor是一个迭代器指针
            //从cursor指向的数据源获取音频文件的id
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
            //从cursor指向的数据源获取音频文件的标题
            val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
            val artist =
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
            val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
            //从cursor指向的数据源获取音频文件的时长
            val duration =
                cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))

            audioFiles.add(AudioFile(id, title, artist, path, duration))//将获取到的音频文件信息添加到音频文件列表中
        }
        cursor.close()
    }

    return audioFiles
}
