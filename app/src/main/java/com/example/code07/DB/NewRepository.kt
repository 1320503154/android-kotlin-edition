package com.example.code07.DB

import android.content.Context
import androidx.annotation.WorkerThread

class NewRepository(context: Context) {
    private var newDao: NewDao? = null

    init {
        // 获取到数据库操作的Dao层接口
        newDao = MyDatabase.getDB(context).getNewDao()
    }

    // Android-Room要求数据操作不能在UI线程中，需要在子线程中进行操作
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertNew(new: New) {
        newDao?.insertNew(new)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getAllNew(): List<New>? {
        return newDao?.getAll()
    }
}
