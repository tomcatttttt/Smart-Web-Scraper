package com.nikita.smartwebscraper.data.repository

import com.nikita.smartwebscraper.data.model.PageStatus
import com.nikita.smartwebscraper.data.model.WebPageNode
import com.nikita.smartwebscraper.data.parser.HtmlParser
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class WebScraper(
    private val maxThreads: Int,
    private val htmlParser: HtmlParser
) {
    private val threadPool = Executors.newFixedThreadPool(maxThreads).asCoroutineDispatcher()
    private val scope = CoroutineScope(threadPool)
    private var isPaused = false
    private var isStopped = false
    private val visitedUrls = mutableSetOf<String>() // Відслідковуємо всі відвідані URL

    // Метод для сканування сторінок
    suspend fun scrape(startUrl: String, searchText: String, maxUrls: Int): List<WebPageNode> {
        val root = WebPageNode(url = startUrl)
        val pendingUrls = mutableListOf(root)
        val results = mutableListOf<WebPageNode>()

        while (pendingUrls.isNotEmpty() && visitedUrls.size < maxUrls && !isStopped) {
            if (isPaused) {
                delay(1000) // Затримка під час паузи
                continue
            }

            val currentNode = pendingUrls.removeAt(0)

            // Якщо це посилання вже відвідували, пропускаємо його
            if (visitedUrls.contains(currentNode.url)) continue

            visitedUrls.add(currentNode.url) // Додаємо до відвіданих URL

            // Асинхронно обробляємо поточну сторінку
            scope.launch {
                processPage(currentNode, searchText)?.let { children ->
                    // Додаємо нові посилання в чергу для подальшого парсингу
                    pendingUrls.addAll(children)
                }
            }.join() // Очікуємо завершення обробки поточної сторінки перед обробкою нових

            // Додаємо результат для поточного URL
            results.add(currentNode)
        }

        threadPool.close()
        return results
    }

    // Метод для обробки сторінки та пошуку тексту
    private suspend fun processPage(node: WebPageNode, searchText: String): List<WebPageNode>? {
        val html = downloadHtml(node.url)

        if (html != null) {
            // Шукаємо текст у видимому контенті
            if (htmlParser.containsText(html, searchText)) {
                node.status = PageStatus.FOUND
                node.matches = htmlParser.countMatches(html, searchText)
            } else {
                node.status = PageStatus.NOT_FOUND
            }

            // Витягуємо всі посилання зі сторінки та додаємо їх як нові вузли
            val childLinks = htmlParser.extractLinks(html).map { WebPageNode(it, parentUrl = node.url) }
            node.children.addAll(childLinks)
            return childLinks
        } else {
            node.status = PageStatus.ERROR
            return null
        }
    }

    // Метод для завантаження HTML-коду сторінки
    private suspend fun downloadHtml(url: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.instanceFollowRedirects = true
                connection.connect()
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    connection.inputStream.bufferedReader().use { it.readText() }
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    // Метод для отримання загальної кількості відвіданих URL
    fun getTotalUrls(): Int {
        return visitedUrls.size
    }

    // Метод для паузи сканування
    fun pauseScraping() {
        isPaused = true
    }

    // Метод для відновлення сканування після паузи
    fun resumeScraping() {
        isPaused = false
    }

    // Метод для зупинки сканування
    fun stopScraping() {
        isStopped = true
    }
}
