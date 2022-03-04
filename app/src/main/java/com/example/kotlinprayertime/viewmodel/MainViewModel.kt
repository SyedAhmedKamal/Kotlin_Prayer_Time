package com.example.kotlinprayertime.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinprayertime.datamodel.MainContainer
import com.example.kotlinprayertime.repository.MainRepository
import com.example.kotlinprayertime.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
) : ViewModel() {

    //TODO("ViewModel logic")
    //TODO("Inject geolocation class into viewModel")

    private val _apiHandler = MutableLiveData<Resource<MainContainer>>()
    val apiHandler: LiveData<Resource<MainContainer>> get() = _apiHandler

    val _city = MutableLiveData<String>()
    val _country = MutableLiveData<String>()

    init {
        getDataFromRepo()
    }

    private fun getDataFromRepo() = viewModelScope.launch(Dispatchers.IO) {

        Log.d("timing", ": ViewModel: ${_city.value}${_country.value}")

        _apiHandler.postValue(Resource.loading(null))

        repository.getDataFromRepo("Karachi", "Pakistan").let {

            if (it.isSuccessful) {
                _apiHandler.postValue(Resource.success(it.body()))
            } else {
                _apiHandler.postValue(Resource.error(it.errorBody().toString(), null))
            }

        }
    }

}