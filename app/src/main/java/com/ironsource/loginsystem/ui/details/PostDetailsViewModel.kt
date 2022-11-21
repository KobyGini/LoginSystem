package com.ironsource.loginsystem.ui.details

import androidx.lifecycle.ViewModel
import com.ironsource.loginsystem.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel
@Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val commentsFlow = repository.getPosts()
}