package com.example.myproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.R
import com.example.myproject.Order


class MyOrdersAdapter(private val list: ArrayList< Order>) :
    RecyclerView.Adapter<MyOrdersAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.tvItemName)
        val quantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val delivery: TextView = itemView.findViewById(R.id.tvDelivery)
        val payment: TextView = itemView.findViewById(R.id.tvPayment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = list[position]

        holder.itemName.text = order.itemName
        holder.quantity.text = "Qty: ${order.quantity}"
        holder.delivery.text = "Arriving on: ${order.deliveryDate}"
        holder.payment.text = "Payment: ${order.paymentStatus}"
    }

    override fun getItemCount(): Int = list.size
}