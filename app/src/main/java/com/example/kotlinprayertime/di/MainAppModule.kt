package com.example.kotlinprayertime.di

import com.example.kotlinprayertime.apiservices.APIHelper
import com.example.kotlinprayertime.apiservices.AdhanAPIImple
import com.example.kotlinprayertime.apiservices.AdhanApi
import com.example.kotlinprayertime.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainAppModule {

    @Singleton
    val client = OkHttpClient().newBuilder().callTimeout(1, TimeUnit.MINUTES).build()

    @Singleton
    @Provides
    fun providesRetrofitInstance(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    @Singleton
    @Provides
    fun providesAPIService(retrofit: Retrofit) = retrofit.create(AdhanApi::class.java)

    @Singleton
    @Provides
    fun providesAPIImple(adhanAPIImple: AdhanAPIImple): APIHelper = adhanAPIImple

}