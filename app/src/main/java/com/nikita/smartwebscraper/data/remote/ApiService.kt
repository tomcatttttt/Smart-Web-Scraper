package com.nikita.smartwebscraper.data.remote


import com.nikita.smartwebscraper.data.model.SearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search")
    suspend fun search(@Query("query") query: String): List<SearchResult>
}
