package ru.netology.nMediaApp.adapter

import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nMediaApp.R
import ru.netology.nMediaApp.databinding.CardPostBinding
import ru.netology.nMediaApp.dto.CountService
import ru.netology.nMediaApp.dto.Post

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            textViewHeader.text = post.autor
            textViewPublishDate.text = post.published
            textViewPostText.text = post.content
            imageViewNonLike.setImageResource(
                if (post.likedByMe) R.drawable.like_red else R.drawable.ic_baseline_favorite_border_24
            )
            imageViewEllipsis.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when(item.itemId){
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }

            textViewLikeCount.text = CountService.countServise(post.likes)

            imageViewNonLike.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            imageViewShare.setOnClickListener {
                onInteractionListener.onShare(post)
            }
            textViewShareCount.text = CountService.countServise(post.share)
        }
    }
}
