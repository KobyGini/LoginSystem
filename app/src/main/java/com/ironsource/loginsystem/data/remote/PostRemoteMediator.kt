package com.ironsource.loginsystem.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ironsource.loginsystem.data.local.LoginSystemDatabase
import com.ironsource.loginsystem.data.local.RemoteKeys
import com.ironsource.loginsystem.data.models.Post
import com.ironsource.loginsystem.util.constants.NetworkConstants.DEFAULT_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator(
    private val remoteDataSource: RemoteDataSource,
    private val loginSystemDatabase: LoginSystemDatabase
) : RemoteMediator<Int, Post>() {

    override suspend fun initialize(): InitializeAction {
        // Require that remote REFRESH is launched on initial load and succeeds before launching
        // remote PREPEND / APPEND.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, Post>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {

                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {

            val response = makeRequest(page)

            val isEndOfList = response.isEmpty()

            loginSystemDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    clearTables()
                }

                val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                insertTables(response, keys)

            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun makeRequest(page: Int): List<Post> {
        return remoteDataSource.getPosts(
            page = page,
        ).body() ?: emptyList()
    }

    private suspend fun clearTables() {
        loginSystemDatabase.remoteKeysDao().clearRemoteKeys()
        loginSystemDatabase.postsDao().deleteAllPosts()
    }

    private suspend fun insertTables(response: List<Post>, keys: List<RemoteKeys>) {
        loginSystemDatabase.remoteKeysDao().insertAll(keys)
        loginSystemDatabase.postsDao().insertAllPosts(response)
    }

    /**
     * get the last remote key inserted which had the data
     */
    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Post>): RemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { doggo -> loginSystemDatabase.remoteKeysDao().remoteKeysMovieId(doggo.id) }
    }

    /**
     * get the first remote key inserted which had the data
     */
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Post>): RemoteKeys? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { doggo -> loginSystemDatabase.remoteKeysDao().remoteKeysMovieId(doggo.id) }
    }

    /**
     * get the closest remote key inserted which had the data
     */
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Post>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                loginSystemDatabase.remoteKeysDao().remoteKeysMovieId(repoId)
            }
        }
    }
}

