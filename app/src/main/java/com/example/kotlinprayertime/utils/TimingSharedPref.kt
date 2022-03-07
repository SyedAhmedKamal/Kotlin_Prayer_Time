package com.example.kotlinprayertime.utils

import android.content.SharedPreferences

object TimingSharedPref {



    fun setFajarTime(time:String){

        val hour = time.substring(0, 2).toInt() // ->23<-:00
        val minute = time.substring(3, 5).toInt() // 23:->00<-


    }

}