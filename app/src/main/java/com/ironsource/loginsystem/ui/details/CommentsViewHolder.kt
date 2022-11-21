package com.ironsource.loginsystem.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ironsource.loginsystem.data.models.Comment
import com.ironsource.loginsystem.databinding.ItemCommentListBinding

class CommentsViewHolder(
    private val binding: ItemCommentListBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(comment: Comment?, onMovieClick: View.OnClickListener?) {
        binding.postTitleTextView.text = comment?.email.toString()
        binding.postBodyTextView.text = comment?.body.toString()

        binding.root.setOnClickListener {
            onMovieClick?.onClick(it)
        }
    }

    companion object {
        fun create(viewGroup: ViewGroup): CommentsViewHolder {
            val binding = ItemCommentListBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
            return CommentsViewHolder(
                binding
            )
        }
    }
}