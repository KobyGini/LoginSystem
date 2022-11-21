package com.ironsource.loginsystem.data.remote

import com.ironsource.loginsystem.data.models.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface JsonPlaceHolderApi {

    @GET("posts")
    suspend fun getPosts(
        @Query("_page") page: Int
    ): Response<List<Post>>
}