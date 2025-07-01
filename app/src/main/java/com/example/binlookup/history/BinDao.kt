package com.example.binlookup.history

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(binEntity: BinEntity)

    @Query("SELECT*FROM bin_history ORDER BY time_search DESC")
    fun getBinsHistory(): List<BinEntity>
}