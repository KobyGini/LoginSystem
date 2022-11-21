package com.ironsource.loginsystem.data.remote

import com.ironsource.loginsystem.data.models.Post
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource
@Inject constructor(
    private val jsonPlaceHolderApi:JsonPlaceHolderApi
){
    suspend fun getPosts(page:Int): Response<List<Post>> {
       return jsonPlaceHolderApi.getPosts(page)
    }

    suspend fun getComments(page:Int): Response<List<Post>> {
       return jsonPlaceHolderApi.getPosts(page)
    }
}