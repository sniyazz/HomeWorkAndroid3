package ru.netology.nMediaApp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nMediaApp.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {
    private var nextId = 1L
    private var posts = listOf(
        Post(
            id = nextId++,
            autor = "Нетология. Меняем карьеру через образование",
            published = "17 ноя в 19:00",
            content = "О преимуществах профессии тестировщика вы наверняка слышали. В этом видео Любовь Маясова, руководитель группы по тестированию цифровых каналов в Райффайзен Банке, рассказывает о главных недостатках профессии.",
            likedByMe = false,
            shareByMe = false,
            likes = 3099,
            share = 2195
        ),
        Post(
            id = nextId++,
            autor = "Нетология. Меняем карьеру через образование",
            published = "18 ноя в 10:00",
            content = "Не работайте в кровати!\n" +
                    "Как правильно организовать рабочий процесс и пространство на фрилансе, где лучше общаться с заказчиками, как правильно хранить файлы — рассказываем в Медиа: https://netolo.gy/k4q",
            likedByMe = false,
            shareByMe = false,
            likes = 2099,
            share = 1195
        ),
        Post(
            id = nextId++,
            autor = "Нетология. Меняем карьеру через образование",
            published = "18 ноя в 19:00",
            content = "В офисе или дома каждому сотруднику необходимо обустроенное рабочее место. Вместительный стол, мягкий стул, складная подставка для ноутбука помогут сделать работу удобной и продуктивной. Что важно при организации рабочего места?\n" +
                    "\n" +
                    "Выберите из вариантов ниже или расскажите в комментариях.",
            likedByMe = false,
            shareByMe = false,
            likes = 3099,
            share = 895
        ),
        Post(
            id = nextId++,
            autor = "Нетология. Меняем карьеру через образование",
            published = "30 октября 2022",
            content = "Если вы умеете находить баги в играх, выводить «Hello, world» в консоли, бэкапить резервные копии, то вы уже немного разработчик. Попробуйте себя в профессии 7 ноября в 19:00 на бесплатном занятии «Что нужно уметь разработчику».  На лекции рассказываем: · как изменился рынок труда в IT, · почему спрос на разработчиков продолжает стремительно расти, · какие направления востребованы в 2022 году.  Присоединяйтесь: https://netolo.gy/kQT",
            likedByMe = false,
            shareByMe = false,
            likes = 1099,
            share = 995
        ),
    ).reversed()

    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data
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
