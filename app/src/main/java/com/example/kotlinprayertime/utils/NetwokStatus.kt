package com.example.kotlinprayertime.utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NetworkStatus(
    private val context: Context
) : LiveData<Boolean>() {

    private val TAG = "NetworkStatus"

    private val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)

            CoroutineScope(Dispatchers.IO).launch {

                if (InternetAvailability.check()) {
                    postValue(true)
                    Log.i(TAG, "onAvailable: Internet access is available")
                } else {
                    postValue(false)
                    Log.i(TAG, "onAvailable: internet in unavailable")
                }
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            Log.i(TAG, "onLost: Internet is not unavailable")
            postValue(false)
        }
    }


    override fun onActive() {
        super.onActive()
        val builder = NetworkRequest.Builder()
        cm.registerNetworkCallback(builder.build(), networkCallback)
    }

    override fun onInactive() {
        super.onInactive()
        cm.unregisterNetworkCallback(networkCallback)
    }
}