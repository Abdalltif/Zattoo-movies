package com.zattoo.movies.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NetworkUtils(private val context: Context) : ConnectivityManager.NetworkCallback() {

    private val networkLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var isConnected = false
    private lateinit var connectivityManager: ConnectivityManager

    fun getNetworkLiveData(): LiveData<Boolean> {
        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(this)
        } else {
            val builder = NetworkRequest.Builder()
            connectivityManager.registerNetworkCallback(builder.build(), this)
        }

        isConnected = false

        connectivityManager.allNetworks.forEach { network ->
            val networkCapability = connectivityManager.getNetworkCapabilities(network)

            networkCapability?.let {
                if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    isConnected = true
                    return@forEach
                }
            }
        }

        networkLiveData.postValue(isConnected)

        return networkLiveData
    }

    fun unRegisterNetworkCallback() = connectivityManager.unregisterNetworkCallback(this)

    override fun onAvailable(network: Network) {
        networkLiveData.postValue(true)
    }

    override fun onLost(network: Network) {
        networkLiveData.postValue(false)
    }

    override fun onUnavailable() {
        super.onUnavailable()
        networkLiveData.postValue(false)
    }
}