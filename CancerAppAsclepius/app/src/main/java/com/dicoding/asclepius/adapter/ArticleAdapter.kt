package com.dicoding.asclepius.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.remote.response.ArticleItem
import com.dicoding.asclepius.databinding.ItemArticleBinding

class ArticleAdapter: ListAdapter<ArticleItem, ArticleAdapter.ListViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class ListViewHolder(private var binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: ArticleItem) {
            Glide.with(binding.root)
                .load(user.urlToImage)
                .apply(RequestOptions().override(80, 80).placeholder(R.drawable.image_icon))
                .transform(CircleCrop())
                .into(binding.tvImage)

            binding.tvTitle.text = user.title
            binding.tvDescription.text = user.description

        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<ArticleItem>() {
            override fun areItemsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}