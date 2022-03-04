package com.example.kotlinprayertime.utils

import android.util.Log
import java.net.InetSocketAddress
import java.net.Socket

class InternetAvailability {

    companion object{

        private val TAG = "InternetAvailability"
        fun check(): Boolean {

            return try {
                val socket = Socket()
                socket.connect(InetSocketAddress("8.8.8.8", 53))
                socket.close()
                Log.i(TAG, "check: access")
                true
            } catch (e: Exception) {
                e.printStackTrace()
                Log.i(TAG, "check: no access")
                false
            }
        }
    }
}