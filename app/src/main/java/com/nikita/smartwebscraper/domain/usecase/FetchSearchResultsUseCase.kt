package com.nikita.smartwebscraper.domain.usecase

import com.nikita.smartwebscraper.data.model.SearchResult
import com.nikita.smartwebscraper.data.repository.SearchRepository
import com.nikita.smartwebscraper.data.repository.WebScraper
import kotlinx.coroutines.flow.Flow

class FetchSearchResultsUseCase(
    private val repository: SearchRepository,
    private val webScraper: WebScraper
) {

    suspend operator fun invoke(query: String, startUrl: String, maxUrls: Int): Flow<List<SearchResult>> {
        return repository.search(query, startUrl, maxUrls)
    }

    fun getTotalUrls(): Int {
        return webScraper.getTotalUrls()
    }

    fun pauseSearch() {
        webScraper.pauseScraping()
    }

    fun stopSearch() {
        webScraper.stopScraping()
    }

    fun resumeSearch() {
        webScraper.resumeScraping()
    }
}
