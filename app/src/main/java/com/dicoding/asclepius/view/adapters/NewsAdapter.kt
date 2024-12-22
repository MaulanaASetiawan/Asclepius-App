package com.dicoding.asclepius.view.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.ItemNewsBinding

class NewsAdapter: ListAdapter<ArticlesItem, NewsAdapter.MyViewHolder>(DIFF_CALLBACK) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return MyViewHolder(binding)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val news = getItem(position)
            holder.bind(news)
        }

        class MyViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(news: ArticlesItem) {
                binding.txtNameTitle.text = news.title
                binding.txtDesc.text = news.description
                Glide.with(binding.root.context)
                    .load(news.urlToImage)
                    .transform(RoundedCorners(16))
                    .into(binding.imgNews)
                binding.root.setOnClickListener {
                    val context = binding.root.context
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(news.url))
                    context.startActivity(intent)
                }
            }
        }

        fun setNews(news: List<ArticlesItem>?) {
            if (news != null) {
                submitList(news)
            }
        }

        companion object {
            private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
                override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                    return oldItem.title == newItem.title
                }

                override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                    return oldItem == newItem
                }
            }
        }
}