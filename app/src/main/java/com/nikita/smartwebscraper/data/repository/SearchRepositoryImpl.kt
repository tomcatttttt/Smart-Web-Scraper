package com.nikita.smartwebscraper.data.repository

import com.nikita.smartwebscraper.data.model.PageStatus
import com.nikita.smartwebscraper.data.model.SearchResult
import com.nikita.smartwebscraper.data.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SearchRepositoryImpl(private val remoteDataSource: RemoteDataSource) : SearchRepository {

    private var isPaused = false
    private var isStopped = false

    override suspend fun search(query: String, startUrl: String, maxUrls: Int): Flow<List<SearchResult>> = flow {
        val visitedUrls = mutableSetOf<String>()
        val pendingUrls = mutableListOf(startUrl)

        val results = mutableListOf<SearchResult>()

        while (pendingUrls.isNotEmpty() && visitedUrls.size < maxUrls && !isStopped) {
            if (isPaused) {
                delay(1000)
                continue
            }

            val url = pendingUrls.removeAt(0)

            if (visitedUrls.contains(url)) continue

            val searchResult = SearchResult(title = url, url = url, status = PageStatus.PROCESSING)
            results.add(searchResult)
            emit(results.toList())

            val foundResult = remoteDataSource.search(url, query)

            if (foundResult != null) {
                searchResult.title = foundResult.title
                searchResult.status = PageStatus.FOUND
                searchResult.matches = foundResult.matches
            } else {
                searchResult.status = PageStatus.NOT_FOUND
            }

            visitedUrls.add(url)

            val links = remoteDataSource.extractLinks(url)
            pendingUrls.addAll(links)

            delay(1000)
            emit(results.toList())
        }
    }.flowOn(Dispatchers.IO)

    override fun pauseSearch() {
        isPaused = true
    }

    override fun resumeSearch() {
        isPaused = false
    }

    override fun stopSearch() {
        isStopped = true
    }
}
