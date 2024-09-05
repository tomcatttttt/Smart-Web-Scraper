package com.nikita.smartwebscraper.di

import com.nikita.smartwebscraper.data.remote.ApiService
import com.nikita.smartwebscraper.data.remote.RemoteDataSource
import com.nikita.smartwebscraper.data.repository.SearchRepository
import com.nikita.smartwebscraper.data.repository.SearchRepositoryImpl
import com.nikita.smartwebscraper.domain.usecase.FetchSearchResultsUseCase
import com.nikita.smartwebscraper.presentation.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single {
        Retrofit.Builder()
            .baseUrl("https://example.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    single { RemoteDataSource() }

    single<SearchRepository> { SearchRepositoryImpl(get()) }

    single { FetchSearchResultsUseCase(get()) }

    viewModel { SearchViewModel(get()) }
}
