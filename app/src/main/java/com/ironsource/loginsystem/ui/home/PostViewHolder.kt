package com.ironsource.loginsystem.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ironsource.loginsystem.data.models.Post
import com.ironsource.loginsystem.databinding.ItemPostListBinding

class PostViewHolder(
    private val binding: ItemPostListBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post?, onPostClick: PostAdapter.OnPostClickListener?) {

        binding.postTitleTextView.text = post?.title
        binding.postBodyTextView.text = post?.body

        binding.root.setOnClickListener {
            post?.let {
                onPostClick?.onPostClick(it)
            }
        }
    }

    companion object {
        fun create(viewGroup: ViewGroup): PostViewHolder {
            val binding = ItemPostListBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
            return PostViewHolder(
                binding
            )
        }
    }
}