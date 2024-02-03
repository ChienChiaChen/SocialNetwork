package com.example.socialnetwork.presentation.create_post

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialnetwork.common.SingleSharedFlow
import com.example.socialnetwork.common.states.StandardTextFieldState
import com.example.socialnetwork.common.wrapper.DataResult
import com.example.socialnetwork.domain.usecase.post.CreatePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val createPost: CreatePostUseCase
) : ViewModel() {

    private val _descriptionState = mutableStateOf(StandardTextFieldState())
    val descriptionState: State<StandardTextFieldState> = _descriptionState
    private val _effect = SingleSharedFlow<CreatePostContract.CreatePostEffect>()
    val effect: SharedFlow<CreatePostContract.CreatePostEffect> = _effect

    fun onEvent(event: CreatePostContract.CreatePostEvent) {
        when (event) {
            is CreatePostContract.CreatePostEvent.EnteredDescription ->
                _descriptionState.value = _descriptionState.value.copy(text = event.value)

            is CreatePostContract.CreatePostEvent.EnteredImageUri ->
                _descriptionState.value = _descriptionState.value.copy(imageUri = event.value)

            is CreatePostContract.CreatePostEvent.Post -> viewModelScope.launch {
                val state = _descriptionState.value
                val postResult = createPost.invoke(state.text, state.imageUri)
                when (postResult.result) {
                    is DataResult.Success<*> ->
                        _effect.tryEmit(CreatePostContract.CreatePostEffect.NavigateTo)

                    is DataResult.Error<*> -> {
                    }

                    null -> return@launch
                }
            }


        }
    }
}