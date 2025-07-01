package com.example.binlookup.hilt

import android.content.Context
import androidx.room.Room
import com.example.binlookup.history.BinDao
import com.example.binlookup.history.BinDatabase
import com.example.binlookup.repository.BeginRepository
import com.example.binlookup.repository.BinRepository
import com.example.binlookup.retrofitApi.BinApiBuilder
import com.example.binlookup.retrofitApi.BinInfoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    fun provideBinApi(): BinInfoApi = BinApiBuilder.api

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): BinDatabase =
        Room.databaseBuilder(context, BinDatabase::class.java, "bin_history").build()

    @Provides
    fun provideDao(db: BinDatabase) = db.binDao()

    @Provides
    fun provideRepository(api: BinInfoApi, dao: BinDao): BinRepository =
        BeginRepository(api, dao)
}