package ru.netology.nMediaApp.viewmodel

import android.app.Application
import androidx.core.os.persistableBundleOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nMediaApp.dto.Post
import ru.netology.nMediaApp.repository.PostRepository
import ru.netology.nMediaApp.repository.PostRepositoryInMemoryImpl

private val empty = Post(
    id = 0,
    content = "",
    autor = "Me",
    likedByMe = false,
    shareByMe = false,
    published = "",
    likes = 0,
    share = 0,
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryInMemoryImpl(application )
    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removedById(id: Long) = repository.removeById(id)
    fun save(){
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }
    fun edit(post: Post){
        edited.value = post
    }
    fun changeContent(content: String){
        val text = content.trim()
        if(edited.value?.content == text){
            return
        }
        edited.value = edited.value?.copy(content = text)
    }
}
