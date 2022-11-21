package com.ironsource.loginsystem.auth

import com.ironsource.loginsystem.model.User
import kotlinx.coroutines.flow.StateFlow

interface AuthService {
    val userStateFlow: StateFlow<UserAuthState>
    suspend fun loginWithEmail(email:String,password:String):Result<User>?
    suspend fun logout()
}

sealed class UserAuthState {
    object Authenticated : UserAuthState()
    object UnAuthenticated : UserAuthState()
}
