package com.softgames.iotec.presentation.authentication.login

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.view.View
import com.softgames.iotec.base.BaseActivity
import com.softgames.iotec.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    override fun setViewBinding() = ActivityLoginBinding.inflate(layoutInflater)

    private lateinit var network_callback: ConnectivityManager.NetworkCallback

    override fun launchEvents() {
        observeInternetConnection()
    }

    private fun observeInternetConnection() {

        network_callback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                runOnUiThread {
                    binding.apply {
                        layoutLogin.visibility = View.VISIBLE
                        layoutNoInternet.visibility = View.GONE

                    }
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                runOnUiThread {
                    binding.apply {
                        layoutLogin.visibility = View.GONE
                        layoutNoInternet.visibility = View.VISIBLE
                        animNoInternet.playAnimation()
                    }
                }
            }
        }

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cm.registerDefaultNetworkCallback(network_callback)
        } else {
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
            cm.registerNetworkCallback(networkRequest, network_callback)
        }
    }

    override fun onPause() {
        //PARAR EL CALLBACK DE LA CONECIVIDAD
        (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            .unregisterNetworkCallback(network_callback)
        super.onPause()
    }

}


