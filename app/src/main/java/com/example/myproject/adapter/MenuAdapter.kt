package com.example.myproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.R
import com.example.myproject.Service2Data

class MenuAdapter(
    private var mList: List<Service2Data>,
    private val onAddClick: (Service2Data) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemImage: ImageView = itemView.findViewById(R.id.menuItemImage)
        val itemTitle: TextView = itemView.findViewById(R.id.menuItemName)
        val itemDescription: TextView = itemView.findViewById(R.id.menuItemDescription)
        val itemPrice: TextView = itemView.findViewById(R.id.menuItemPrice)
        val addButton: Button = itemView.findViewById(R.id.addButton)

        fun bind(item: Service2Data) {
            itemImage.setImageResource(item.imageRes)
            itemTitle.text = item.title
            itemDescription.text = item.description
            itemPrice.text = item.price

            addButton.setOnClickListener {
                onAddClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.menu_item, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int = mList.size

    // Optional: for search/filter
    fun setFilterList(filteredList: List<Service2Data>) {
        mList = filteredList
        notifyDataSetChanged()
    }
}
