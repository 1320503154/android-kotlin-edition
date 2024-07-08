package com.example.code07.DB

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [New::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun newDao(): NewDao

    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getDB(context: Context): MyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "my_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }

        fun clearData() {
            INSTANCE?.clearAllTables()
        }
    }
}
