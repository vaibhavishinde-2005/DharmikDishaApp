package com.example.myproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.databinding.ActivityCartItemBinding

class CartAdapter(
    private val cartItems: MutableList<String>,
    private val cartService: MutableList<String>,
    private val cartItemPrices: MutableList<String>,
    private val cartImages: MutableList<String>
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>()  {

    class CartViewHolder(val binding: ActivityCartItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ActivityCartItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val itemName = cartItems[position]
        val itemPrice = cartItemPrices[position]
        val itemService = cartService[position]
        val itemImage = cartImages[position]

        holder.binding.ServiceName.text = itemName
        holder.binding.ServicerName.text = itemService
        holder.binding.price.text = itemPrice

        // ✔ FIXED IMAGE LOADING
        val context = holder.binding.root.context
        val resId = context.resources.getIdentifier(itemImage, "drawable", context.packageName)

        if (resId != 0) {
            holder.binding.imageView3.setImageResource(resId)
        } else {
            holder.binding.imageView3.setImageResource(android.R.drawable.ic_menu_report_image)
        }

        // ✔ Delete button
        holder.binding.deleteButton.setOnClickListener {
            cartItems.removeAt(position)
            cartItemPrices.removeAt(position)
            cartService.removeAt(position)
            cartImages.removeAt(position)

            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartItems.size)
        }
    }
}
