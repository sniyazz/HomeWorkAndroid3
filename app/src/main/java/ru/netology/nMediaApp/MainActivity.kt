package ru.netology.nMediaApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import ru.netology.nMediaApp.R
import ru.netology.nMediaApp.adapter.OnInteractionListener
import ru.netology.nMediaApp.adapter.PostAdapter
import ru.netology.nMediaApp.databinding.ActivityMainBinding
import ru.netology.nMediaApp.dto.Post
import ru.netology.nMediaApp.util.AndroidUtils
import ru.netology.nMediaApp.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removedById(post.id)
            }

        })
        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
        viewModel.edited.observe(this) { post ->
            if (post.id == 0L) {
                return@observe
            }
            with(binding.content) {
                requestFocus()
                setText(post.content)
                binding.editCancel.visibility = View.VISIBLE
            }
        }
        binding.save.setOnClickListener {
            with(binding.content) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        context.getString(R.string.error_empty_content),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                viewModel.changeContent(text.toString())
                viewModel.save()

                setText("")
                clearFocus()
                binding.editCancel.visibility = View.GONE
                AndroidUtils.hideKeyboard(this)
            }
        }
        binding.editCancel.setOnClickListener{
            binding.content.text.clear()
            viewModel.save()
            binding.editCancel.visibility = View.GONE

        }
    }
}
