package com.ironsource.loginsystem.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ironsource.loginsystem.auth.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    private var _loginSharedFlow: MutableSharedFlow<LoginShardFlow> = MutableSharedFlow()
    val loginSharedFlow = _loginSharedFlow.asSharedFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val response = authService.loginWithEmail(
                email, password
            )
            response?.let {
                if (it.isFailure) {
                    it.exceptionOrNull()?.message?.let {
                        _loginSharedFlow.emit(
                            LoginShardFlow.Error(it)
                        )
                    }
                }
            }
        }
    }

    sealed class LoginShardFlow {
        data class Error(val massage: String):LoginShardFlow()
    }
}

