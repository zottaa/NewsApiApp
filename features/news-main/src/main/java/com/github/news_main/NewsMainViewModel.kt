package com.github.news_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
internal class NewsMainViewModel @Inject constructor(
    private val getAllArticleUseCase: Provider<GetAllArticleUseCase>,
) : ViewModel() {

    val state: StateFlow<State> =
        getAllArticleUseCase.get().invoke(query = "Android")
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, State.Initial)

    fun forceUpdate() {
    }
}