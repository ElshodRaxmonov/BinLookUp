package com.example.binlookup.history

import android.content.Context
import androidx.room.Database
import androidx.room.InvalidationTracker
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BinEntity::class], version = 1, exportSchema = false)
abstract class BinDatabase : RoomDatabase() {
    abstract fun binDao(): BinDao

    companion object {
        @Volatile
        private var instance: BinDatabase? = null

        fun getDatabase(context: Context): BinDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    BinDatabase::class.java,
                    "bin_history"
                ).build().also { instance = it }
            }
    }
}