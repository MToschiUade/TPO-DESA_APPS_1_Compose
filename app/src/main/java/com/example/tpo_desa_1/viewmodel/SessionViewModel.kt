package com.example.tpo_desa_1.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SessionViewModel : ViewModel() {
    val isLoggedIn = mutableStateOf(false)

    fun logIn() {
        isLoggedIn.value = true
    }

    fun logOut() {
        isLoggedIn.value = false
    }
}
