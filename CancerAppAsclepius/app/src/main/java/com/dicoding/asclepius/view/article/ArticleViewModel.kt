package com.dicoding.asclepius.view.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.Result
import com.dicoding.asclepius.data.remote.response.ArticleItem
import com.dicoding.asclepius.data.repository.ArticleRepository

class ArticleViewModel(articleRepository: ArticleRepository): ViewModel(){

    val listUser: LiveData<Result<List<ArticleItem>>> = articleRepository.getListUser()
}