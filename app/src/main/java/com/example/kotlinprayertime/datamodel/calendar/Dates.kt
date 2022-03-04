package com.example.kotlinprayertime.datamodel.calendar

import com.example.kotlinprayertime.datamodel.calendar.gregorean.GregorianDate
import com.example.kotlinprayertime.datamodel.calendar.hijricalendar.Hijri

data class Dates(
    val hijri:Hijri,
    val gregorian:GregorianDate
)
