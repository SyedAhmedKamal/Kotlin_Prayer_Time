package com.example.kotlinprayertime.datamodel

import com.example.kotlinprayertime.datamodel.calendar.Dates
import com.example.kotlinprayertime.datamodel.timing.TimingModel

data class Data(
    val timings: TimingModel,
    val date: Dates
)
