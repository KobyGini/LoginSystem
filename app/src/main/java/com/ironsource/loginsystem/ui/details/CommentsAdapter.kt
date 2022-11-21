package com.ironsource.loginsystem.ui.details

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ironsource.loginsystem.data.models.Comment

class CommentsAdapter :
    ListAdapter<Comment, CommentsViewHolder>(USER_AND_ALBUMS_COMPARATOR) {

    companion object {
        private val USER_AND_ALBUMS_COMPARATOR = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment) =
                oldItem == newItem
        }
    }

    private var onAlbumClick: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        return CommentsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.bind(getItem(position),onAlbumClick)
    }

}