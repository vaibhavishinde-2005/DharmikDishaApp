package com.example.myproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.R
import com.example.myproject.Service1Data

class ServiceAdapter(
    private var mList: List<Service1Data>,
    private val onBookClick: (Service1Data) -> Unit
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    inner class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val logo: ImageView = itemView.findViewById(R.id.imageView3)
        val titleTv: TextView = itemView.findViewById(R.id.ServiceName)
        val serviceTv: TextView = itemView.findViewById(R.id.ServicerName)
        val experienceTv: TextView = itemView.findViewById(R.id.yrsofexperience) // 🔥 FIX
        val bookBtn: Button = itemView.findViewById(R.id.bookButton)

        fun bind(item: Service1Data) {
            logo.setImageResource(item.logo)
            titleTv.text = item.title
            serviceTv.text = item.service

            // 🔥 SHOW EXPERIENCE
            experienceTv.text = "Experience: ${item.experience}"

            bookBtn.setOnClickListener {
                onBookClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_each, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int = mList.size

    // 🔍 Used for search filter
    fun setFilterList(filteredList: List<Service1Data>) {
        mList = filteredList
        notifyDataSetChanged()
    }
}
