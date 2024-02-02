package com.example.socialnetwork.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var firstDraw by remember { mutableStateOf(0) }
    if (firstDraw == 0) {
        viewModel.onEvent(LoginContract.LoginEvent.HasUser)
    }
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                is LoginContract.LoginEffect.HasUser -> {
                    if (it.value) {
                        navController.navigate(Screen.MainFeedScreen.route)
                        firstDraw++
                    }

                }

                LoginContract.LoginEffect.NavigateTo -> navController.navigate(Screen.MainFeedScreen.route)
            }

        }
    }

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
                text = stringResource(id = R.string.login_page_login),
                style = MaterialTheme.typography.h1
            )
            Spacer(modifier = Modifier.height(SpaceMedium))
            StandardTextField(
                text = state.emailText,
                onValueChange = {
                    viewModel.onEvent(LoginContract.LoginEvent.EnteredEmail(it))
                },
                keyboardType = KeyboardType.Email,
                error = when (state.emailError) {
                    LoginState.EmailError.FieldEmpty -> stringResource(id = R.string.login_page_invalid_empty)
                    LoginState.EmailError.Invalid -> stringResource(id = R.string.login_page_invalid_empty)
                    null -> ""
                },
                hint = stringResource(id = R.string.login_hint)
            )
            Spacer(modifier = Modifier.height(SpaceMedium))
            StandardTextField(
                text = state.passwordText,
                onValueChange = {
                    viewModel.onEvent(LoginContract.LoginEvent.EnteredPassword(it))
                },
                hint = stringResource(id = R.string.login_page_password_hint),
                keyboardType = KeyboardType.Password,
                error = when (state.passwordError) {
                    LoginState.PasswordError.FieldEmpty -> {
                        stringResource(id = R.string.register_page_invalid_empty)
                    }

                    LoginState.PasswordError.InputTooShort -> {
                        stringResource(
                            id = R.string.register_page_invalid_too_short,
                            Constants.MIN_PASSWORD_LENGTH
                        )
                    }

                    LoginState.PasswordError.Invalid -> {
                        stringResource(id = R.string.register_page_invalid_password)
                    }

                    null -> ""
                },
                isPasswordVisible = state.isPasswordVisible,
                onPasswordToggleClick = {
                    viewModel.onEvent(LoginContract.LoginEvent.TogglePasswordVisibility)
                }
            )
            Spacer(modifier = Modifier.height(SpaceMedium))
            Button(
                onClick = {
                    viewModel.onEvent(LoginContract.LoginEvent.Login)
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = stringResource(id = R.string.login_page_login),
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }
        Text(
            text = buildAnnotatedString {
                append(stringResource(id = R.string.login_page_dont_have_an_account_yet))
                append(" ")
                val signUpText = stringResource(id = R.string.login_page_sign_up)
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colors.primary
                    )
                ) {
                    append(signUpText)
                }
            },
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clickable {
                    navController.navigate(
                        Screen.RegisterScreen.route
                    )
                }
        )
    }

}