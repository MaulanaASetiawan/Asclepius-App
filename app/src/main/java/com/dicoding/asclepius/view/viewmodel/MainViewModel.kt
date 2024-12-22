package com.dicoding.asclepius.view.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.BookmarkRepository
import com.dicoding.asclepius.data.local.entity.Bookmarks
import kotlinx.coroutines.launch

class MainViewModel(private val repository: BookmarkRepository) : ViewModel() {
    fun getNews() = repository.getNews(page = 1, pageSize = 50)
    fun getBookmark() = repository.getAllBookmark()
    fun setBookmark(bookmarks: Bookmarks) {
        viewModelScope.launch {
            repository.insertBookmark(bookmarks)
        }
    }
}

class PredictViewModel : ViewModel() {
    var currentImage: Uri? = null
}