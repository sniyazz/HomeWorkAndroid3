package ru.netology.nMediaApp.repository

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nMediaApp.dto.Post
import java.lang.reflect.Type

class PostRepositoryInMemoryImpl(
    context: Context
) : PostRepository {

    companion object{
        private const val POST_KEY = "POST_KEY"
    }

    private var nextId = 1L
    private var posts: List<Post> = emptyList()
        set(value){
            field = value
            sync()
        }
    val  prefs = context.getSharedPreferences("posts", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

    private val data = MutableLiveData(posts)

    init {
        readPosts()
    }

    override fun getAll(): LiveData<List<Post>> = data

    private fun sync() {
        prefs.edit {
            putString(POST_KEY, gson.toJson(posts))
        }
    }

    private fun readPosts(){
        posts = prefs.getString(POST_KEY, null)?.let {
            gson.fromJson<List<Post>>(it, type)
        }.orEmpty()
        data.value = posts
    }
    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) {
                it
            } else {
                var count = if (!it.likedByMe) it.copy(likes = it.likes + 1)
                else it.copy(likes = it.likes - 1)
                it.copy(likedByMe = !it.likedByMe, likes = count.likes)
            }
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it
            else it.copy(share = it.share + 1, shareByMe = it.shareByMe)
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            posts = listOf(
                post.copy(
                    id = nextId++,
                    autor = "Me",
                    published = "Now",
                    likedByMe = false,
                    shareByMe = false,
                    likes = 0,
                    share = 0
                )
            ) + posts
            data.value = posts
            return
        }
        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
    }
}
