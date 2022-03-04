package com.example.kotlinprayertime.apiservices

import com.example.kotlinprayertime.datamodel.MainContainer
import retrofit2.Response

interface APIHelper {
    suspend fun getDataHelper(city: String, country: String): Response<MainContainer>
}