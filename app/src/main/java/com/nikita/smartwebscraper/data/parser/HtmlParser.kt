package com.nikita.smartwebscraper.data.parser

class HtmlParser {

    // Метод для пошуку тексту на сторінці
    fun containsText(html: String, searchText: String): Boolean {
        val visibleText = extractVisibleText(html)
        println("Шукаємо текст '$searchText' у видимому тексті сторінки...")
        return visibleText.contains(searchText, ignoreCase = true)
    }

    // Метод для підрахунку збігів тексту у видимому контенті
    fun countMatches(html: String, searchText: String): Int {
        val visibleText = extractVisibleText(html)
        val regex = Regex(Regex.escape(searchText), RegexOption.IGNORE_CASE)
        return regex.findAll(visibleText).count()
    }

    // Метод для витягання тільки видимого тексту сторінки (без тегів)
    private fun extractVisibleText(html: String): String {
        // Видаляємо теги <script>, <style> та інші невидимі елементи, які не містять видимого тексту
        var cleanedHtml = html.replace(Regex("<(script|style|head|noscript|meta)[^>]*?>.*?</\\1>", RegexOption.IGNORE_CASE), "")

        // Видаляємо всі HTML теги
        cleanedHtml = cleanedHtml.replace(Regex("<[^>]+>"), " ")

        // Видаляємо зайві пробіли та повертаємо тільки текстовий контент
        return cleanedHtml.trim().replace(Regex("\\s+"), " ")
    }

    // Метод для витягання посилань зі сторінки
    fun extractLinks(html: String): List<String> {
        val regex = Regex("""https?://(www\.)?[-a-zA-Z0-9@:%._\+~#=]{2,256}\.[a-z]{2,6}\b([-a-zA-Z0-9@:%_\+.~#?&//=]*)""")
        return regex.findAll(html).map { it.value }.toList()
    }
}
