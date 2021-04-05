package com.acmvit.c2c2021

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import com.google.firebase.FirebaseApp
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.newFixedThreadPoolContext
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.*

var isConnected: Boolean = false

class C2CApp: Application() {
    private lateinit var connectivityManager: ConnectivityManager

    companion object {
        private val _isConnectedObservable: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
        val isConnectedObservable: Observable<Boolean>
            get() = _isConnectedObservable
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        getSystemService<ConnectivityManager>()?.let {
            connectivityManager = it
        }

        isConnected = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true

        startKoin {
            androidContext(this@C2CApp)
            modules(listOf(appModule, repoModule))
        }

        connectivityManager.registerDefaultNetworkCallback(ConnectivityCallback())
    }

    inner class ConnectivityCallback : ConnectivityManager.NetworkCallback() {
        override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
            val connected = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            _isConnectedObservable.onNext(connected)
            isConnected = connected
        }

        override fun onLost(network: Network) {
            if (connectivityManager.activeNetwork == null) {
                _isConnectedObservable.onNext(false)
                isConnected = false
            }
        }
    }
}