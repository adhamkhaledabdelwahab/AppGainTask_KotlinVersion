package kh.ad.appgaintask.view_model

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NetworkViewModel : ViewModel() {
    val connected = MutableLiveData<Boolean>()

    fun registerNetworkStateObserver(app: Application) {
        val manager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        manager.registerNetworkCallback(networkRequest, object : NetworkCallback() {
            val availableNetworks: MutableSet<Network> = HashSet()
            override fun onAvailable(network: Network) {
                availableNetworks.add(network)
                connected.postValue(true)
            }

            override fun onLost(network: Network) {
                availableNetworks.remove(network)
                connected.postValue(!availableNetworks.isEmpty())
            }

            override fun onUnavailable() {
                availableNetworks.clear()
                connected.postValue(false)
            }
        })
    }
}
