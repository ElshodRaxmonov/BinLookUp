package com.example.binlookup.retrofitApi

import com.example.binlookup.model.BinInfo
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface BinInfoApi {

    @GET("/{bin}")
    suspend fun getBinInfo(@Path("bin") bin: String): retrofit2.Response<BinInfo>

}