package com.nikita.smartwebscraper.di

import com.nikita.smartwebscraper.data.parser.HtmlParser
import com.nikita.smartwebscraper.data.remote.RemoteDataSource
import com.nikita.smartwebscraper.data.repository.SearchRepository
import com.nikita.smartwebscraper.data.repository.SearchRepositoryImpl
import com.nikita.smartwebscraper.data.repository.WebScraper
import com.nikita.smartwebscraper.domain.usecase.FetchSearchResultsUseCase
import com.nikita.smartwebscraper.presentation.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { HtmlParser() }

    single { RemoteDataSource(get()) }

    single { WebScraper(maxThreads = 10, htmlParser = get()) }

    // Repository
    single<SearchRepository> { SearchRepositoryImpl(get()) }

    single { FetchSearchResultsUseCase(get(), get()) }

    // ViewModel
    viewModel { SearchViewModel(get()) }
}
