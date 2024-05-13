package com.github.news_main

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.size.Size
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
    val context = LocalContext.current

    Row(Modifier.padding(bottom = 4.dp)) {
        var imageVisible by remember { mutableStateOf(true) }
        if (imageVisible) {
            AsyncImage(
                model = article.urlToImage,
                onError = {
                    imageVisible = false
                },
                contentDescription = stringResource(R.string.content_description_article_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(150.dp)
            )
        }
        Spacer(modifier = Modifier.size(4.dp))
        Column(
            modifier = Modifier
                .padding(8.dp)
                .clickable {
                    followTheLink(context, article.url)
                }
        ) {
            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 1
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = article.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                textAlign = TextAlign.Justify
            )
        }
    }
}

private fun followTheLink(context: Context, url: String) {
    Intent(Intent.ACTION_VIEW, Uri.parse(url)).also {
        context.startActivity(it)
    }
}
