package com.dicoding.asclepius.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.dicoding.asclepius.data.local.entity.Bookmarks
import com.dicoding.asclepius.databinding.ItemHistoryBinding
import java.text.NumberFormat

class PredictAdapter : ListAdapter<Bookmarks, PredictAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bookmarks = getItem(position)
        holder.bind(bookmarks)
    }

    class ViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bookmarks: Bookmarks) {
            binding.txtLabel.text = bookmarks.label
            binding.txtConfidence.text = NumberFormat.getPercentInstance().format(bookmarks.confidence).trim()
            Glide.with(binding.root.context)
                .load(bookmarks.image)
                .transform(RoundedCorners(16))
                .into(binding.imgHistory)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Bookmarks>() {
            override fun areItemsTheSame(oldItem: Bookmarks, newItem: Bookmarks): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Bookmarks, newItem: Bookmarks): Boolean {
                return oldItem == newItem
            }
        }
    }
}
