package com.example.binlookup.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.binlookup.history.BinDao
import com.example.binlookup.history.BinEntity
import com.example.binlookup.model.Bank
import com.example.binlookup.model.BinInfo
import com.example.binlookup.model.Country
import com.example.binlookup.model.Number
import com.example.binlookup.retrofitApi.BinInfoApi
import com.google.gson.Gson
import java.util.Collections.list
import javax.inject.Inject

class BeginRepository @Inject constructor(private val api: BinInfoApi, private val dao: BinDao) :
    BinRepository {
    override suspend fun getInfo(bin: String): BinInfo {
        val response = api.getBinInfo(bin)
        val responseBody = response.body()
        if (response.isSuccessful) {
            if (responseBody != null) {
                responseBody.let {
                    val json = Gson().toJson(responseBody)
                    dao.insert(
                        BinEntity(
                            bin = bin,
                            searchTime = System.currentTimeMillis(),
                            jsonOfData = json
                        )
                    )
                    return BinInfo(
                        bank = it.bank,
                        brand = it.brand.orEmpty(),
                        country = Country(
                            it.country.alpha2.orEmpty(),
                            it.country.currency.orEmpty(),
                            it.country.emoji,
                            it.country.latitude.or(0),
                            it.country.longitude.or(0),
                            it.country.name.orEmpty(),
                            it.country.numeric.orEmpty()
                        ),
                        number = Number(
                            length = it.number.length.or(0),
                            luhn = it.number.luhn.or(false)
                        ),
                        prepaid = it.prepaid.or(false),
                        scheme = it.scheme.orEmpty(),
                        type = it.type.orEmpty()


                    )
                }
                return responseBody
            } else {

                throw Exception("Empty Response body")
            }
        } else {

            throw Exception("API error:${response.code()}")

        }

    }

    override suspend fun getHistory(): List<BinEntity> {
        return dao.getBinsHistory()
    }


}