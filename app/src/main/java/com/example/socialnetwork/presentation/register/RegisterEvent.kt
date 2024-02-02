package com.example.socialnetwork.presentation.register

class RegisterContract {
    sealed class RegisterEvent {
        data class EnteredUsername(val value: String) : RegisterEvent()
        data class EnteredPassword(val value: String) : RegisterEvent()
        data class EnteredConfirmPassword(val value: String) : RegisterEvent()
        data class EnteredEmail(val value: String) : RegisterEvent()
        object TogglePasswordVisibility : RegisterEvent()
        object Register : RegisterEvent()
    }

    sealed class RegisterEffect {
        object NavigateTo : RegisterEffect()
    }
}
