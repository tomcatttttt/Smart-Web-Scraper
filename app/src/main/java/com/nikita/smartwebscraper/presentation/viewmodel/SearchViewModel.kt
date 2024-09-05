package com.nikita.smartwebscraper.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikita.smartwebscraper.data.model.SearchResult
import com.nikita.smartwebscraper.domain.usecase.FetchSearchResultsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SearchViewModel(private val fetchSearchResultsUseCase: FetchSearchResultsUseCase) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResults: StateFlow<List<SearchResult>> = _searchResults

    private val _progress = MutableStateFlow(0)
    val progress: StateFlow<Int> = _progress

    fun search(query: String, url: String, maxThreads: Int, maxUrls: Int) {
        viewModelScope.launch {
            fetchSearchResultsUseCase(query, url, maxUrls)
                .catch { e ->
                }
                .collect { results ->
                    _searchResults.value = results
                    _progress.value = (_searchResults.value.size * 100) / maxUrls
                }
        }
    }

    fun pauseSearch() {
        fetchSearchResultsUseCase.pauseSearch()
    }

    fun stopSearch() {
        fetchSearchResultsUseCase.stopSearch()
    }

    fun resumeSearch() {
        fetchSearchResultsUseCase.resumeSearch()
    }
}
