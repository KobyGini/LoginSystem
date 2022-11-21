package com.ironsource.loginsystem.auth

import com.ironsource.loginsystem.model.User
import com.ironsource.loginsystem.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class AuthServiceImpl
@Inject constructor(
    private val repository: Repository
) : AuthService {

    private var _userStateFlow = MutableStateFlow<UserAuthState>(UserAuthState.UnAuthenticated)

    override val userStateFlow: StateFlow<UserAuthState>
        get() = _userStateFlow

    init {
        repository.getUser()?.let {
            _userStateFlow.update { UserAuthState.Authenticated }
        }
    }

    override suspend fun loginWithEmail(email: String, password: String): Result<User> {
        val response = repository.loginWithEmail(email, password)
        if (response.isSuccess) {
            _userStateFlow.update { UserAuthState.Authenticated }
        }
        return response
    }

    override suspend fun logout() {
        repository.logout()
        _userStateFlow.update { UserAuthState.UnAuthenticated }
    }
}