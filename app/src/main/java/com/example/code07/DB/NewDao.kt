package com.example.code07.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NewDao {
    @Insert
    suspend fun insertNew(new: New)

    @Query("SELECT * FROM news")
    suspend fun getAllNew(): List<New>?
}
