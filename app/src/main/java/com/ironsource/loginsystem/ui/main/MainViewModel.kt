package com.ironsource.loginsystem.ui.main

import androidx.lifecycle.ViewModel
import com.ironsource.loginsystem.auth.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    fun getAuthStateFlow() = authService.userStateFlow

}