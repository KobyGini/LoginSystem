package com.ironsource.loginsystem.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comment_table")
data class Comment(
    val postId: Int,
    @PrimaryKey
    val id: Int,
    val name: Int,
    val email: Int,
    val body: Int
)