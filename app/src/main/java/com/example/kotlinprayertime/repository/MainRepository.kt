package com.example.kotlinprayertime.repository

import com.example.kotlinprayertime.apiservices.APIHelper
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiHelper: APIHelper
) {
    suspend fun getDataFromRepo(city: String, country: String) =
        apiHelper.getDataHelper(city, country)
}