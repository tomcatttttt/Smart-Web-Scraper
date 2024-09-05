package com.nikita.smartwebscraper.data.repository

import com.nikita.smartwebscraper.data.model.SearchResult
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun search(query: String, startUrl: String, maxUrls: Int): Flow<List<SearchResult>>

    fun pauseSearch()
    fun stopSearch()
    fun resumeSearch()
}
