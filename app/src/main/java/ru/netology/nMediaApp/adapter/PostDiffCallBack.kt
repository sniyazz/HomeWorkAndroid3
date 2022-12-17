package ru.netology.nMediaApp.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.netology.nMediaApp.dto.Post

class PostDiffCallBack : DiffUtil.ItemCallback<Post>() {
    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}
