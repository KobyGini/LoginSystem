package com.ironsource.loginsystem.data.local

import android.content.Context
import androidx.paging.PagingSource
import com.ironsource.loginsystem.data.models.Comment
import com.ironsource.loginsystem.data.models.Post
import com.ironsource.loginsystem.model.User
import com.ironsource.loginsystem.util.FileHelper
import com.ironsource.loginsystem.util.SharedPrefManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalDataSource
@Inject constructor(
    private val fileHelper: FileHelper,
    private val sharedPrefManager: SharedPrefManager,
    @ApplicationContext private val context: Context,
    private val database:LoginSystemDatabase
) {

    private val postsDao = database.postsDao()
    private val commentsDao = database.commentsDao()

    suspend fun getAllUsers(): List<User>? =
        withContext(Dispatchers.IO) {
            fileHelper.readAssetFile<List<User>>(context, "users.json")
        }

    fun saveUser(user: User) {
        sharedPrefManager.saveUser(user)
    }

    fun removeUser(){
        sharedPrefManager.removeUser()
    }

    fun getUser(): User? {
        return sharedPrefManager.getUser()
    }

    fun getPosts(): PagingSource<Int, Post> {
        return postsDao.getPosts()
    }

    fun getPostsAndComments(): Flow<List<Comment>> {
        return commentsDao.get()
    }
}