package com.ironsource.loginsystem.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ironsource.loginsystem.data.local.LocalDataSource
import com.ironsource.loginsystem.data.local.LoginSystemDatabase
import com.ironsource.loginsystem.data.models.Comment
import com.ironsource.loginsystem.data.models.Post
import com.ironsource.loginsystem.data.remote.PostRemoteMediator
import com.ironsource.loginsystem.data.remote.RemoteDataSource
import com.ironsource.loginsystem.model.User
import com.ironsource.loginsystem.util.constants.NetworkConstants.DEFAULT_PAGE_SIZE
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl
@Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val loginSystemDatabase: LoginSystemDatabase
) : Repository {

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = true)
    }

    override fun getUser(): User? {
        return localDataSource.getUser()
    }

    override suspend fun loginWithEmail(email: String, password: String): Result<User> {
        val response = localDataSource.getAllUsers()
        val user = response?.find {
            it.email == email && it.password == password
        }
        delay(1000)
        return user?.let {
            localDataSource.saveUser(it)
            Result.success(it)
        } ?: Result.failure(Throwable("user not found"))
    }

    override suspend fun logout() {
        localDataSource.removeUser()
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPosts(): Flow<PagingData<Post>> {
        return Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = { localDataSource.getPosts() },
            remoteMediator = PostRemoteMediator(
                remoteDataSource = remoteDataSource,
                loginSystemDatabase = loginSystemDatabase,
            )
        ).flow
    }

    override fun getComments(): Flow<List<Comment>> {
        localDataSource.getPosts()
    }
}