package com.example.kotlinprayertime.apiservices

import com.example.kotlinprayertime.datamodel.MainContainer
import com.example.kotlinprayertime.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AdhanApi {

    @GET(Constants.ENDPOINT)
    suspend fun getApiData(@Query("city") city: String, @Query("country") country: String): Response<MainContainer>

}