package com.ironsource.loginsystem.repository

import androidx.paging.PagingData
import com.ironsource.loginsystem.data.models.Comment
import com.ironsource.loginsystem.data.models.Post
import com.ironsource.loginsystem.model.User
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getUser(): User?
    suspend fun loginWithEmail(email: String, password: String):Result<User>
    suspend fun logout()
     fun getPosts():Flow<PagingData<Post>>
     fun getComments():Flow<List<Comment>>

}