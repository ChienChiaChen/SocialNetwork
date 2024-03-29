package com.example.socialnetwork.presentation.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.socialnetwork.R
import com.example.socialnetwork.common.Constants
import com.example.socialnetwork.common.Screen
import com.example.socialnetwork.common.components.StandardTextField
import com.example.socialnetwork.ui.theme.SpaceLarge
import com.example.socialnetwork.ui.theme.SpaceMedium
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            navController.navigate(Screen.LoginScreen.route)
        }
    }

    if (state.isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.onBackground
            )
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = SpaceLarge,
                    end = SpaceLarge,
                    top = SpaceLarge,
                    bottom = 50.dp
                )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
            ) {
                Text(
                    text = stringResource(id = R.string.register_page_register),
                    style = MaterialTheme.typography.h1
                )
                Spacer(modifier = Modifier.height(SpaceMedium))
                StandardTextField(
                    text = state.emailText,
                    onValueChange = {
                        viewModel.onEvent(RegisterContract.RegisterEvent.EnteredEmail(it))
                    },
                    error = when (state.emailError) {
                        RegisterState.EmailError.FieldEmpty -> {
                            stringResource(id = R.string.register_page_invalid_empty)
                        }

                        RegisterState.EmailError.Invalid -> {
                            stringResource(id = R.string.register_page_invalid_email)
                        }

                        null -> ""
                    },
                    keyboardType = KeyboardType.Email,
                    hint = stringResource(id = R.string.register_page_email)
                )
                Spacer(modifier = Modifier.height(SpaceMedium))
                StandardTextField(
                    text = state.usernameText,
                    onValueChange = {
                        viewModel.onEvent(RegisterContract.RegisterEvent.EnteredUsername(it))
                    },
                    error = when (state.usernameError) {
                        RegisterState.UsernameError.FieldEmpty -> {
                            stringResource(id = R.string.register_page_invalid_empty)
                        }

                        RegisterState.UsernameError.InputTooShort -> {
                            stringResource(
                                id = R.string.register_page_invalid_too_short,
                                Constants.MIN_USERNAME_LENGTH
                            )
                        }

                        RegisterState.UsernameError.Invalid -> {
                            stringResource(id = R.string.register_page_invalid)
                        }

                        null -> ""
                    },
                    hint = stringResource(id = R.string.register_page_username)
                )
                Spacer(modifier = Modifier.height(SpaceMedium))
                StandardTextField(
                    text = state.passwordText,
                    onValueChange = {
                        viewModel.onEvent(RegisterContract.RegisterEvent.EnteredPassword(it))
                    },
                    hint = stringResource(id = R.string.register_page_password_hint),
                    keyboardType = KeyboardType.Password,
                    error = when (state.passwordError) {
                        RegisterState.PasswordError.FieldEmpty -> {
                            stringResource(id = R.string.register_page_invalid_empty)
                        }

                        RegisterState.PasswordError.InputTooShort -> {
                            stringResource(
                                id = R.string.register_page_invalid_too_short,
                                Constants.MIN_PASSWORD_LENGTH
                            )
                        }

                        RegisterState.PasswordError.Invalid -> {
                            stringResource(id = R.string.register_page_invalid_password)
                        }

                        null -> ""
                    },
                    isPasswordVisible = state.isPasswordVisible,
                    onPasswordToggleClick = {
                        viewModel.onEvent(RegisterContract.RegisterEvent.TogglePasswordVisibility)
                    }
                )
                Spacer(modifier = Modifier.height(SpaceMedium))
                StandardTextField(
                    text = state.confirmPasswordText,
                    onValueChange = {
                        viewModel.onEvent(RegisterContract.RegisterEvent.EnteredConfirmPassword(it))
                    },
                    hint = stringResource(id = R.string.register_page_confirmed_password_hint),
                    keyboardType = KeyboardType.Password,
                    error = when (state.confirmPasswordError) {
                        RegisterState.ConfirmPasswordError.FieldEmpty -> {
                            stringResource(id = R.string.register_page_invalid_empty)
                        }

                        RegisterState.ConfirmPasswordError.InputTooShort -> {
                            stringResource(
                                id = R.string.register_page_invalid_too_short,
                                Constants.MIN_PASSWORD_LENGTH
                            )
                        }

                        RegisterState.ConfirmPasswordError.Invalid,
                        RegisterState.ConfirmPasswordError.NotMatch -> {
                            stringResource(id = R.string.register_page_invalid_password)
                        }

                        null -> ""
                    },
                    isPasswordVisible = state.isPasswordVisible,
                    onPasswordToggleClick = {
                        viewModel.onEvent(RegisterContract.RegisterEvent.TogglePasswordVisibility)
                    }
                )
                Spacer(modifier = Modifier.height(SpaceMedium))
                Button(
                    onClick = {
                        viewModel.onEvent(RegisterContract.RegisterEvent.Register)
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                ) {
                    Text(
                        text = stringResource(id = R.string.register_page_register),
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
            Text(
                text = buildAnnotatedString {
                    append(stringResource(id = R.string.register_page_already_have_an_account))
                    append(" ")
                    val signUpText = stringResource(id = R.string.register_page_sign_in)
                    withStyle(
                        style = SpanStyle(color = MaterialTheme.colors.primary)
                    ) {
                        append(signUpText)
                    }
                },
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .clickable {
                        navController.popBackStack()
                    }
            )
        }
    }
}