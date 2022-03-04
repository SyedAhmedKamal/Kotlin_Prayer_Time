package com.example.kotlinprayertime.apiservices

import com.example.kotlinprayertime.datamodel.MainContainer
import retrofit2.Response
import javax.inject.Inject

class AdhanAPIImple @Inject constructor(
    private val api: AdhanApi
) : APIHelper {
    override suspend fun getDataHelper(city: String, country: String): Response<MainContainer> =
        api.getApiData(city, country)
}