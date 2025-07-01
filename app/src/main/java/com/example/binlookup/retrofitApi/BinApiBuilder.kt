package com.example.binlookup.retrofitApi

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BinApiBuilder {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://lookup.binlist.net")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api: BinInfoApi = retrofit.create(BinInfoApi::class.java)
}