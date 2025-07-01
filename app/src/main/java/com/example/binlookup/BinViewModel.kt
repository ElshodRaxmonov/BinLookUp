package com.example.binlookup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.binlookup.history.BinEntity
import com.example.binlookup.model.BinInfo
import com.example.binlookup.repository.BinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject


@HiltViewModel
class BinViewModel @Inject constructor(private val binRepository: BinRepository) : ViewModel() {
    val history = MutableLiveData<List<BinEntity>>()
    val inputBinCode = MutableLiveData<String>()
    val schemeName = MutableLiveData<String>("Not available")
    val cardType = MutableLiveData<String>("Not available")
    val cardBrand = MutableLiveData<String>("Not available")

    val countryCode = MutableLiveData<String>()
    val countryName = MutableLiveData<String>("Not available")
    val latitude = MutableLiveData<Int>(0)
    val longitude = MutableLiveData<Int>(0)

    val bankName = MutableLiveData<String>("Not available")
    val bankWebsite = MutableLiveData<String>("Not available")
    val bankPhone = MutableLiveData<String>("Not available")
    val city = MutableLiveData<String>("Not available")

    private val _binInformation = MutableLiveData<BinInfo>()
    val binInformation: LiveData<BinInfo> = _binInformation
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        getHistory()
    }

    fun getBinInfo() {
        Log.d("MAIN", "getBinInfo: ${_binInformation.value}")
        viewModelScope.launch {

            try {
                Log.d("TAG", "Inside try block")

                val result = binRepository.getInfo(inputBinCode.value.toString())

                Log.d("TAG", "After getInfo")

                _binInformation.value = result
                Log.d("TAG", "RESULT:$result")

                setAllInformation()
                getHistory()
            } catch (e: Exception) {
                Log.e("TAG", "Exception type: ${e::class.simpleName}")
                Log.e("TAG", "Exception message: ${e.message}", e)
                _errorMessage.value = "Error: ${e.message ?: "Unknown"}"

            }
        }
    }

    fun getHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = binRepository.getHistory()
            withContext(Dispatchers.Main) {
                history.value = list
            }
        }
    }

    private fun setAllInformation() {
        val info = _binInformation.value ?: return

        with(info) {
            countryName.value = country.name
            schemeName.value = scheme
            countryCode.value = country.alpha2
            cardType.value = type
            cardBrand.value = brand
            bankName.value = bank.name
            bankWebsite.value = bank.url
            bankPhone.value = bank.phone
            city.value = bank.city
            longitude.value = country.longitude
            latitude.value = country.latitude
        }
    }

    fun getUrlForFlag(): String =
        "https://flagsapi.com/${countryCode.value!!.uppercase()}/flat/64.png"

    fun checkValidNumbers(): Boolean {
        return if (inputBinCode.value?.length == 6 || inputBinCode.value?.length == 8) true else false
    }


}