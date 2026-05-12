package com.example.myproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.databinding.ServiceBinding

class PopularAdapter(
    private val items: List<String>,
    private val images: List<Int>,
    private val onItemClick: (position: Int) -> Unit
) : RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(
            ServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClick
        )
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        holder.bind(items[position], images[position])
    }

    override fun getItemCount() = items.size

    class PopularViewHolder(
        private val binding: ServiceBinding,
        onItemClick: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }

        fun bind(item: String, imageRes: Int) {
            binding.ServiceName.text = item
            binding.imageView3.setImageResource(imageRes)
        }
    }
}
