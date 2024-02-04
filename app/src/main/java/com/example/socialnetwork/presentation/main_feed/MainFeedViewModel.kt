package com.example.socialnetwork.presentation.main_feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialnetwork.common.wrapper.DataResult
import com.example.socialnetwork.domain.Post
import com.example.socialnetwork.domain.usecase.post.FetchCurrentUserPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFeedViewModel @Inject constructor(
    private val fetchCurrentUserPostUseCase: FetchCurrentUserPostUseCase,
) : ViewModel() {

    private val _mainFeedDataState = MutableStateFlow(MainFeedDataState())
    val mainFeedDataState: StateFlow<MainFeedDataState> = _mainFeedDataState

    fun onEvent(event: MainFeedContract.MainFeedEvent) {
        when (event) {
            is MainFeedContract.MainFeedEvent.RefreshPost -> {
                viewModelScope.launch {
                    when (val profileResult = fetchCurrentUserPostUseCase.invoke()) {
                        is DataResult.Success<List<Post>> -> {
                            _mainFeedDataState.value =
                                _mainFeedDataState.value.copy(post = profileResult.data)
                        }

                        is DataResult.Error<*> -> {
                            // send Error msg
                        }

                        null -> return@launch
                    }
                }
            }
        }

    }
}