package com.github.news_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.news.data.Article
import com.github.news.data.RequestResult
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

internal class NewsMainViewModel(
    getAllArticleUseCase: GetAllArticleUseCase
) : ViewModel() {

    val state: StateFlow<State> =
        getAllArticleUseCase.invoke()
            .stateIn(viewModelScope, SharingStarted.Lazily, State.Initial)
}