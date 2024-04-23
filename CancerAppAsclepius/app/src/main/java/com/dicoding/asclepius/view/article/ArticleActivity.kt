package com.dicoding.asclepius.view.article

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.ArticleAdapter
import com.dicoding.asclepius.databinding.ActivityArticleBinding
import com.dicoding.asclepius.data.Result

class ArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleBinding
    private lateinit var viewModel: ArticleViewModel
    private val adapter by lazy { ArticleAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupRecyclerView()
        observerListUser()
    }

    private fun setupViewModel() {
        val factory: ArticleViewModelFactory = ArticleViewModelFactory.getInstance()

        viewModel = ViewModelProvider(
            this,
            factory
        )[ArticleViewModel::class.java]
    }

    private fun setupRecyclerView() {
        binding.listArticle.setHasFixedSize(true)
        binding.listArticle.layoutManager = LinearLayoutManager(this)
        binding.listArticle.adapter = adapter

    }

    private fun observerListUser() {
        viewModel.listUser.observe(this) {
            when (it) {
                is Result.Loading -> {
                    binding.Loading.isVisible = true
                }

                is Result.Success -> {
                    binding.Loading.isVisible = false
                    adapter.submitList(it.data)
                }

                is Result.Error -> {
                    binding.Loading.isVisible = false
                    Toast.makeText(this@ArticleActivity,"Terjadi Kesalahan, Silahkan Coba Kembali", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}