package com.nikita.smartwebscraper.data.model

data class SearchResult(
    var title: String,
    val url: String,
    var status: PageStatus = PageStatus.NOT_PROCESSED,
    var matches: Int = 0
)

enum class PageStatus {
    NOT_PROCESSED,
    PROCESSING,
    FOUND,
    ERROR,
    NOT_FOUND
}
