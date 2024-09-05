package com.nikita.smartwebscraper.domain.usecase

import com.nikita.smartwebscraper.data.repository.SearchRepository

class FetchSearchResultsUseCase(private val repository: SearchRepository) {

    suspend operator fun invoke(query: String, startUrl: String, maxUrls: Int) = repository.search(query, startUrl, maxUrls)

    fun pauseSearch() {
        repository.pauseSearch()
    }

    fun stopSearch() {
        repository.stopSearch()
    }

    fun resumeSearch() {
        repository.resumeSearch()
    }
}
