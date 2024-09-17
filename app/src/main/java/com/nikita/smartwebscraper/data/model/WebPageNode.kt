package com.nikita.smartwebscraper.data.model


data class WebPageNode(
    val url: String,
    val parentUrl: String? = null,
    val children: MutableList<WebPageNode> = mutableListOf(),
    var status: PageStatus = PageStatus.NOT_PROCESSED,
    var matches: Int = 0
)
