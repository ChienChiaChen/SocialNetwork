package com.example.socialnetwork.presentation.main_feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialnetwork.common.exception.getStringResId
import com.example.socialnetwork.common.wrapper.DataResult
import com.example.socialnetwork.domain.Post
import com.example.socialnetwork.domain.usecase.post.FetchAllPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFeedViewModel @Inject constructor(
    private val fetchAllPostUseCase: FetchAllPostUseCase
) : ViewModel() {

    private val _mainFeedDataState = MutableStateFlow(MainFeedDataState())
    val mainFeedDataState: StateFlow<MainFeedDataState> = _mainFeedDataState

    fun onEvent(event: MainFeedContract.MainFeedEvent) {
        when (event) {
            is MainFeedContract.MainFeedEvent.RefreshPost -> {
                _mainFeedDataState.value = _mainFeedDataState.value.copy(refreshing = true)
                viewModelScope.launch {
                    when (val profileResult = fetchAllPostUseCase.invoke()) {
                        is DataResult.Success<List<Post>> -> {
                            _mainFeedDataState.value =
                                _mainFeedDataState.value.copy(post = profileResult.data)
                        }

                        is DataResult.Error<*> -> {
                            // send Error msg
                            _mainFeedDataState.value =
                                _mainFeedDataState.value.copy(errorMsg = profileResult.exception.getStringResId())
                        }

                        null -> return@launch
                    }
                    _mainFeedDataState.value = _mainFeedDataState.value.copy(refreshing = false)
                }
            }
        }

    }
}