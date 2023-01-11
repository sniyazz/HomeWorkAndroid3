package ru.netology.nMediaApp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.launch
import androidx.activity.viewModels
import ru.netology.nMediaApp.R
import ru.netology.nMediaApp.adapter.OnInteractionListener
import ru.netology.nMediaApp.adapter.PostAdapter
import ru.netology.nMediaApp.databinding.ActivityMainBinding
import ru.netology.nMediaApp.dto.Post
import ru.netology.nMediaApp.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val viewModel: PostViewModel by viewModels()

    val newPostLauncher = registerForActivityResult(NewPostResultContract()) { result ->
        result ?: return@registerForActivityResult
        viewModel.changeContent(result)
        viewModel.save()
    }

    val editPostLauncher = registerForActivityResult(EditPostResultContract()) { result ->
        result ?: return@registerForActivityResult
        viewModel.changeContent(result)
        viewModel.save()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        val adapter = PostAdapter(object : OnInteractionListener {

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                editPostLauncher.launch(post.content)
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

//            override fun onShare(post: Post) {
//                viewModel.shareById(post.id)
//            }

            override fun onRemove(post: Post) {
                viewModel.removedById(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(this)
        { posts ->
            adapter.submitList(posts)
        }

        binding.fab.setOnClickListener {
            newPostLauncher.launch()
        }
    }
}
