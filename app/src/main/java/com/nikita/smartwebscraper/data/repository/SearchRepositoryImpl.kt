package com.nikita.smartwebscraper.data.repository

import com.nikita.smartwebscraper.data.model.SearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(private val webScraper: WebScraper) : SearchRepository {

    override suspend fun search(query: String, startUrl: String, maxUrls: Int): Flow<List<SearchResult>> {
        return flow {
            val results = webScraper.scrape(startUrl, query, maxUrls)
            emit(results.map { webPageNode ->
                SearchResult( 
                    url = webPageNode.url,
                    title = webPageNode.url,
                    status = webPageNode.status,
                    matches = webPageNode.matches
                )
            })
        }
    }

    override fun getTotalUrls(): Int {
        return webScraper.getTotalUrls()
    }

    override fun pauseSearch() {
        webScraper.pauseScraping()
    }

    override fun stopSearch() {
        webScraper.stopScraping()
    }

    override fun resumeSearch() {
        webScraper.resumeScraping()
    }
}
