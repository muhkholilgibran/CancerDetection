package com.dicoding.asclepius.data.repository


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.Result
import com.dicoding.asclepius.data.remote.response.ArticleItem
import com.dicoding.asclepius.data.remote.retrofit.ApiService


class ArticleRepository(
    private val apiService: ApiService
) {

    fun getListUser(): LiveData<Result<List<ArticleItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getArticle("cancer", "health", "en", BuildConfig.API_KEY)
            if (response.status == "ok") {
                emit(Result.Success(response.articles?.filterNotNull() ?: emptyList()))
            } else {
                emit(Result.Error("Error fetching articles"))
            }

        } catch (e: Exception) {
            Log.d("UserRepository", "getListUser: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }
}