package com.example.kotlinprayertime.datamodel.calendar.hijricalendar

import com.google.gson.annotations.SerializedName

data class Hijri(
    @SerializedName("date")
    val hijriDate: String,
    @SerializedName("weekday")
    val hijriWeekDay: HijriWeekDay,
    @SerializedName("month")
    val hijriMonth: HijriMonth
)
