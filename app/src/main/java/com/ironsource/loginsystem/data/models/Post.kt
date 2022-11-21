package com.ironsource.loginsystem.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "post_table")
data class Post(
    val userId: Int,
    @PrimaryKey
    val id: String,
    val title: String,
    val body: String,
)