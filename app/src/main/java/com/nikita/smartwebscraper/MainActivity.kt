package com.nikita.smartwebscraper

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.nikita.smartwebscraper.presentation.ui.SearchScreen
import com.nikita.smartwebscraper.presentation.ui.theme.SmartWebScraperTheme
import com.nikita.smartwebscraper.presentation.viewmodel.SearchViewModel
import org.koin.androidx.compose.getViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isInternetAvailable()) {
            Log.i("InternetCheck", "Підключення до інтернету є")
        } else {
            Log.e("InternetCheck", "Немає підключення до інтернету")
        }

        setContent {
            SmartWebScraperTheme {
                val viewModel = getViewModel<SearchViewModel>()
                SearchScreen(viewModel = viewModel)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}

