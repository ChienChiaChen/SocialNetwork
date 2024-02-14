package com.example.socialnetwork.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialnetwork.common.snackbar.SnackbarManager
import com.example.socialnetwork.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.example.socialnetwork.common.wrapper.DataResult
import com.example.socialnetwork.domain.Post
import com.example.socialnetwork.domain.usecase.auth.user.FetchCurrentUserCase
import com.example.socialnetwork.domain.usecase.auth.user.UpdateCurrentUserCase
import com.example.socialnetwork.domain.usecase.post.FetchCurrentUserPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val fetchCurrentUserPostUseCase: FetchCurrentUserPostUseCase,
    private val fetchCurrentUserCase: FetchCurrentUserCase,
    private val updateCurrentUserCase: UpdateCurrentUserCase
) : ViewModel() {
    private val _toolbarState = MutableStateFlow(ProfileToolbarState())
    val toolbarState: StateFlow<ProfileToolbarState> = _toolbarState

    private val _profileState = MutableStateFlow(ProfileDataState())
    val profileState: StateFlow<ProfileDataState> = _profileState

    fun setExpandedRatio(ratio: Float) {
        _toolbarState.value = _toolbarState.value.copy(expandedRatio = ratio)
        println("UPDATING TOOLBAR STATE TO $toolbarState")
    }

    fun setToolbarOffsetY(value: Float) {
        _toolbarState.value = _toolbarState.value.copy(toolbarOffsetY = value)
        println("UPDATING TOOLBAR STATE TO $toolbarState")
    }

    fun onEvent(event: ProfileContract.ProfileEvent) {
        when (event) {
            is ProfileContract.ProfileEvent.EnteredImageUri -> {
                _profileState.value = _profileState.value.copy(isUserInfoLoading = true)
                viewModelScope.launch {
                    when (val userInfo = updateCurrentUserCase.invoke(event.value)) {
                        is DataResult.Success -> {
                            _profileState.value =
                                _profileState.value.copy(user = userInfo.data)
                        }

                        is DataResult.Error<*> -> SnackbarManager.showMessage(userInfo.exception.toSnackbarMessage())
                    }
                    _profileState.value = _profileState.value.copy(isUserInfoLoading = false)
                }
            }

            is ProfileContract.ProfileEvent.RefreshPost -> {
                _profileState.value = _profileState.value.copy(isLoading = true)
                viewModelScope.launch {
                    when (val profileResult = fetchCurrentUserPostUseCase.invoke()) {
                        is DataResult.Success<List<Post>> -> {
                            _profileState.value =
                                _profileState.value.copy(post = profileResult.data)
                        }

                        is DataResult.Error -> {
                            SnackbarManager.showMessage(profileResult.exception.toSnackbarMessage())
                        }

                        null -> return@launch
                    }
                    _profileState.value = _profileState.value.copy(isLoading = false)
                }
            }

            is ProfileContract.ProfileEvent.RefreshUserInfo -> {
                viewModelScope.launch {
                    when (val userInfo = fetchCurrentUserCase.invoke()) {
                        is DataResult.Success -> {
                            _profileState.value =
                                _profileState.value.copy(user = userInfo.data)
                        }

                        is DataResult.Error -> SnackbarManager.showMessage(userInfo.exception.toSnackbarMessage())
                    }
                }
            }
        }

    }
}