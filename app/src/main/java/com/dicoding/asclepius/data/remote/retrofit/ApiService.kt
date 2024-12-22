package com.dicoding.asclepius.data.remote.retrofit

import retrofit2.http.GET
import retrofit2.http.Query
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.remote.response.NewsResponse

interface ApiService {
    @GET("everything")
    suspend fun getNews(
        @Query("q") query: String = "cancer",
        @Query("sortBy") sortBy: String = "relevancy",
        @Query("page") page: Int = 50,
        @Query("pageSize") pageSize: Int = 1,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): NewsResponse
}