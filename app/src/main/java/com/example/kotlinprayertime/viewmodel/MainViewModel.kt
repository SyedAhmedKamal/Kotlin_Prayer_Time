package com.example.kotlinprayertime.viewmodel

import androidx.lifecycle.ViewModel
import com.example.kotlinprayertime.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    //TODO("ViewModel logic")
    //TODO("Inject geolocation class into viewModel")

}