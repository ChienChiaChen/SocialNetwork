package com.example.socialnetwork.presentation.login

class LoginContract {

    sealed class LoginEvent {
        data class EnteredEmail(val value: String) : LoginEvent()
        data class EnteredPassword(val value: String) : LoginEvent()
        object TogglePasswordVisibility : LoginEvent()
        object Login : LoginEvent()
        object HasUser : LoginEvent()

    }

    sealed class LoginEffect {
        object NavigateTo : LoginEffect()
        data class HasUser(val value: Boolean) : LoginEffect()
    }
}