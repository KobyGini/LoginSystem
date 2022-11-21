package com.ironsource.loginsystem.ui.home

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.ironsource.loginsystem.data.models.Post

class PostAdapter :
    PagingDataAdapter<Post, PostViewHolder>(USER_AND_ALBUMS_COMPARATOR) {

    companion object {
        private val USER_AND_ALBUMS_COMPARATOR = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Post, newItem: Post) =
                oldItem == newItem
        }
    }

    private var onPostClick: OnPostClickListener? = null

    fun setOnPostClickListener(onPostClick: OnPostClickListener) {
        this.onPostClick = onPostClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position), onPostClick)
    }

    interface OnPostClickListener {
        fun onPostClick(post: Post)
    }
}