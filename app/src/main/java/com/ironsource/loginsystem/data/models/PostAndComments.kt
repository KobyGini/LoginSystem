package com.ironsource.loginsystem.data.models

import androidx.room.Embedded
import androidx.room.Relation

data class PostAndComments(
    @Embedded
    val post: Post,

    @Relation(
        parentColumn = "id",
        entityColumn = "postId"
    )
    val habitTask: List<Comment>
)