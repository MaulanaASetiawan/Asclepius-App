package com.dicoding.asclepius.di

import android.content.Context
import com.dicoding.asclepius.data.BookmarkRepository
import com.dicoding.asclepius.data.local.room.BookmarkDb
import com.dicoding.asclepius.data.remote.retrofit.ApiConfig


object Injection {
    fun provideRepository(context: Context): BookmarkRepository {
        val apiService = ApiConfig.getApiService()
        val db = BookmarkDb.getInstance(context)
        val dao = db.bookmarkDao()
        return  BookmarkRepository.getInstance(dao, apiService)
    }
}