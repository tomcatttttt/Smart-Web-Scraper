package com.nikita.smartwebscraper.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nikita.smartwebscraper.data.model.PageStatus
import com.nikita.smartwebscraper.data.model.SearchResult
import com.nikita.smartwebscraper.presentation.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel) {
    var query by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var maxThreads by remember { mutableStateOf("") }
    var maxUrls by remember { mutableStateOf("") }

    val progress by viewModel.progress.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {

            item {
                TextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("Start URL") },
                    singleLine = true, // Однорядкове поле
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                TextField(
                    value = maxThreads,
                    onValueChange = { maxThreads = it },
                    label = { Text("Max Threads") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }

            item {
                TextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Search Text") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }

            item {
                TextField(
                    value = maxUrls,
                    onValueChange = { maxUrls = it },
                    label = { Text("Max Scanned URLs") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }

            item {
                Row(modifier = Modifier.padding(top = 16.dp)) {
                    Button(
                        onClick = { viewModel.search(query, url, maxThreads.toIntOrNull() ?: 1, maxUrls.toIntOrNull() ?: 10) },
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    ) {
                        Text("Start")
                    }
                    Button(
                        onClick = { viewModel.pauseSearch() },
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    ) {
                        Text("Pause")
                    }
                    Button(
                        onClick = { viewModel.stopSearch() },
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    ) {
                        Text("Stop")
                    }
                }
            }

            item {
                LinearProgressIndicator(progress = progress / 100f, modifier = Modifier.fillMaxWidth().padding(top = 16.dp))
            }
            items(searchResults) { result ->
                SearchResultRow(result)
            }
        }
    }
}

@Composable
fun SearchResultRow(result: SearchResult) {
    val backgroundColor = when (result.status) {
        PageStatus.NOT_PROCESSED -> Color.Gray
        PageStatus.PROCESSING -> Color(0xFFFFA500)
        PageStatus.FOUND -> Color.Green
        PageStatus.NOT_FOUND -> Color.Black
        PageStatus.ERROR -> Color.Red
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(8.dp)
    ) {
        Text(
            text = result.title,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "URL: ${result.url}",
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "Matches: ${result.matches}",
            color = Color.White
        )
    }
}
