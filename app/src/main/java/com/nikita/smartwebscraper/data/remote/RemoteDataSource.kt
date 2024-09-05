package com.nikita.smartwebscraper.data.remote

import com.nikita.smartwebscraper.data.model.PageStatus
import com.nikita.smartwebscraper.data.model.SearchResult
import org.jsoup.Jsoup
import java.io.IOException

class RemoteDataSource {

    suspend fun search(url: String, searchText: String): SearchResult? {
        return try {
            val doc = Jsoup.connect(url).get()
            val matches = doc.text().split(searchText).size - 1
            if (matches > 0) {
                SearchResult(title = doc.title(), url = url, status = PageStatus.FOUND, matches = matches)
            } else {
                null
            }
        } catch (e: IOException) {
            null
        }
    }

    suspend fun extractLinks(url: String): List<String> {
        return try {
            val doc = Jsoup.connect(url).get()
            doc.select("a[href]").map { it.attr("abs:href") }
        } catch (e: IOException) {
            emptyList()
        }
    }
}
