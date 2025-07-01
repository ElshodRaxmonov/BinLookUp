package com.example.binlookup.repository

import androidx.lifecycle.LiveData
import com.example.binlookup.history.BinEntity
import com.example.binlookup.model.BinInfo

interface BinRepository {
    suspend fun getInfo(bin: String): BinInfo
    suspend fun getHistory(): List<BinEntity>
}