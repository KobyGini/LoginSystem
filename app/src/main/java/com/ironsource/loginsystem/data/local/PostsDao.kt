package com.ironsource.loginsystem.data.local

import androidx.paging.PagingSource
import androidx.room.*
import com.ironsource.loginsystem.data.models.Post
import com.ironsource.loginsystem.data.models.PostAndComments
import kotlinx.coroutines.flow.Flow

@Dao
interface PostsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPosts(posts: List<Post>)

    @Query("SELECT * FROM post_table WHERE id = :id")
    suspend fun getPostById(id: String): Post

    @Query("DELETE FROM post_table")
    suspend fun deleteAllPosts()

    @Query("SELECT * FROM post_table")
    fun getPosts(): PagingSource<Int, Post>

    @Transaction
    @Query("SELECT * FROM post_table")
    fun getPostsAndComments(): Flow<List<PostAndComments>>

}