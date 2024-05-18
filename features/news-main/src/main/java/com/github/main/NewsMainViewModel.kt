package com.github.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.news.data.Article
import com.github.utility.Dispatcher
import com.github.utility.DispatcherType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
@OptIn(FlowPreview::class)
internal class NewsMainViewModel @Inject constructor(
    private val getAllArticleUseCase: Provider<GetAllArticleUseCase>,
    @Dispatcher(DispatcherType.IO)
    private val dispatcher: CoroutineDispatcher,
    private val forceUpdateUseCase: Provider<ForceUpdateUseCase>
) : ViewModel() {

    val state: StateFlow<State>
        get() = _state
    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Initial)

    val query: StateFlow<String>
        get() = _query
    private val _query: MutableStateFlow<String> = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    private val searchQuery = _query
        .debounce(SEARCH_DELAY)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isNotBlank()) {
                getAllArticleUseCase.get().invoke(query)
            } else {
                flowOf(State.Initial)
            }
        }

    init {
        viewModelScope.launch(dispatcher) {
            searchQuery.collect { responseState ->
                _state.value = responseState
            }
        }
    }

    fun forceUpdate(query: String, currentArticles: List<Article>) {
        viewModelScope.launch(dispatcher) {
            if (query.isNotBlank()) {
                forceUpdateUseCase.get().invoke(query, currentArticles).collect { newState ->
                    _state.value = newState
                }
            }
        }
    }

    fun search(query: String) {
        _query.value = query
    }

    companion object {
        private const val SEARCH_DELAY = 1000L
    }
}
