package com.nikita.smartwebscraper.data.remote

import com.nikita.smartwebscraper.data.model.PageStatus
import com.nikita.smartwebscraper.data.model.SearchResult
import com.nikita.smartwebscraper.data.parser.HtmlParser
import java.net.URL

class RemoteDataSource(private val htmlParser: HtmlParser) {

    suspend fun search(url: String, searchText: String): SearchResult? {
        return try {
            val html = downloadHtml(url)
            if (html != null && htmlParser.containsText(html, searchText)) {
                val matches = htmlParser.countMatches(html, searchText) // Підраховуємо кількість збігів у видимому тексті
                SearchResult(title = url, url = url, status = PageStatus.FOUND, matches = matches)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun extractLinks(url: String): List<String> {
        return try {
            val html = downloadHtml(url)
            if (html != null) {
                htmlParser.extractLinks(html)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun downloadHtml(url: String): String? {
        return try {
            URL(url).readText()
        } catch (e: Exception) {
            null
        }
    }
}
