package com.ironsource.loginsystem.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ironsource.loginsystem.data.models.Comment
import com.ironsource.loginsystem.data.models.Post

@Database(entities = [Post::class,Comment::class,RemoteKeys::class], version = 1)
abstract class LoginSystemDatabase : RoomDatabase() {
    abstract fun postsDao(): PostsDao
    abstract fun commentsDao(): CommentsDao
    abstract fun remoteKeysDao():RemoteKeysDao
}