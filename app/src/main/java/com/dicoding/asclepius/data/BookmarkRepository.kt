package com.dicoding.asclepius.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.asclepius.data.local.entity.Bookmarks
import com.dicoding.asclepius.data.local.room.BookmarkDao
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.data.remote.retrofit.ApiService
import com.dicoding.asclepius.helper.Result

class BookmarkRepository private constructor(
    private val bookmarkDao: BookmarkDao,
    private val apiService: ApiService
) {
    fun getAllBookmark(): LiveData<List<Bookmarks>> = bookmarkDao.getAllBookmark()

    suspend fun insertBookmark(bookmarks: Bookmarks) = bookmarkDao.insertBookmark(bookmarks)

    fun getNews(page: Int, pageSize: Int): LiveData<Result<List<ArticlesItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getNews(page = page, pageSize = pageSize)
            val articles = response.articles?.filterNotNull()
            if (!articles.isNullOrEmpty()) {
                emit(Result.Success(articles))
            } else {
                emit(Result.Error("No articles found"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error occurred"))
        }
    }

    companion object {
        @Volatile
        private var instance: BookmarkRepository? = null

        fun getInstance(bookmarkDao: BookmarkDao, apiService: ApiService): BookmarkRepository =
            instance ?: synchronized(this) {
                instance ?: BookmarkRepository(bookmarkDao, apiService).also { instance = it }
            }
    }
}