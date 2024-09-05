package com.nikita.smartwebscraper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.nikita.smartwebscraper.presentation.ui.SearchScreen
import com.nikita.smartwebscraper.presentation.ui.theme.SmartWebScraperTheme
import com.nikita.smartwebscraper.presentation.viewmodel.SearchViewModel
import org.koin.androidx.compose.getViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SmartWebScraperTheme {
                val viewModel = getViewModel<SearchViewModel>()
                SearchScreen(viewModel = viewModel)
            }
        }
    }
}
