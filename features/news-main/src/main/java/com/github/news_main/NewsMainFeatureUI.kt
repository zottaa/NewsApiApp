package com.github.news_main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.news.data.Article

@Composable
fun NewsMainScreen() {
    NewsMainScreen(viewModel())
}


@Composable
internal fun NewsMainScreen(viewModel: NewsMainViewModel) {
    val state by viewModel.state.collectAsState()
    val currentState = state
    if (currentState != State.Initial) {
        NewsMainContent(currentState)
    }
}

@Composable
private fun NewsMainContent(currentState: State) {
    Column {
        if (currentState is State.Error) {
            ErrorMessage(currentState)
        }
        if (currentState is State.Loading) {
            ProgressIndicator()
        }
        if (currentState.articles.isNotEmpty()) {
            Articles(articles = currentState.articles)
        }
    }
}

@Composable
private fun ErrorMessage(currentState: State.Error) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.error)
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = currentState.errorMessage,
            color = MaterialTheme.colorScheme.onError
        )
    }
}

@Composable
private fun ProgressIndicator() {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Articles(articles: List<Article>) {
    LazyColumn {
        items(articles) { article ->
            Article(article)
        }
    }
}

@Composable
private fun Article(article: Article) {
    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Text(
            text = article.title,
            style = MaterialTheme.typography.headlineMedium,
            maxLines = 1
        )
        Text(
            text = article.content,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 3,
            textAlign = TextAlign.Justify
        )
    }
}
