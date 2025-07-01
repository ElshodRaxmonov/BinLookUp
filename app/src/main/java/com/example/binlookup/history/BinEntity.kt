package com.example.binlookup.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bin_history")
data class BinEntity(
    @PrimaryKey(autoGenerate = false)
    val bin: String,

    @ColumnInfo(name = "json_of_data")
    val jsonOfData: String?,

    @ColumnInfo(name = "time_search")
    val searchTime: Long
)