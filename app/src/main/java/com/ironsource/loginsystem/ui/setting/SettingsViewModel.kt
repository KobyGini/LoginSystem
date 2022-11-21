package com.ironsource.loginsystem.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ironsource.loginsystem.auth.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
@Inject constructor(
    private val authService: AuthService
) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            authService.logout()
        }
    }
}