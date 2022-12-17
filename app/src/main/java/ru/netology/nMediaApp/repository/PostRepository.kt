package ru.netology.nMediaApp.repository

import androidx.lifecycle.LiveData
import ru.netology.nMediaApp.dto.Post

interface PostRepository {

    fun getAll() : LiveData<List<Post>>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)

}
