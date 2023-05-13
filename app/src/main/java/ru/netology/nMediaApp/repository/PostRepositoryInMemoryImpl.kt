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
    val context: Context
) : PostRepository {

    companion object {
        private const val FILE_NAME = "posts.json"
    }

    //private var nextId = 1L
    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

    private var posts: List<Post> = readPosts()
        set(value) {
            field = value
            sync()
        }

    private val data = MutableLiveData(posts)


    override fun getAll(): LiveData<List<Post>> = data

    private fun sync() {
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }

    private fun readPosts(): List<Post> {
        val file = context.filesDir.resolve(FILE_NAME)
        return if (file.exists()){
        context.openFileInput(FILE_NAME).bufferedReader().use {
            gson.fromJson(it, type)
        }
    } else{
        emptyList()
    }
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
                id = (posts.firstOrNull()?.id ?: 0L) + 1,
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
