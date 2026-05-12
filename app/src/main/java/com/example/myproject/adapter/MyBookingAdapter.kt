package com.example.myproject.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.R
import com.example.myproject.data.model.BookingPandit
import com.example.myproject.payment

class MyBookingAdapter(
    private val context: Context,
    private val list: MutableList<BookingPandit>
) : RecyclerView.Adapter<MyBookingAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPanditName: TextView = view.findViewById(R.id.tvPanditName)
        val tvService: TextView = view.findViewById(R.id.tvPanditService)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val btnPay: Button = view.findViewById(R.id.btnPay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_booking, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        // Basic data
        holder.tvPanditName.text = item.panditName.ifEmpty { "Unknown" }
        holder.tvService.text = item.panditService.ifEmpty { "Unknown Service" }
        holder.tvDate.text = item.date.ifEmpty { "-" }
        holder.tvTime.text = item.time.ifEmpty { "-" }

        // Safe status handling
        val statusText = if (item.status.isEmpty()) "Pending" else item.status
        holder.tvStatus.text = statusText

        // ✅ Fixed amount (₹1001)
        val amount = 1001
        holder.btnPay.text = "Pay ₹$amount"

        // Status logic
        when (statusText.lowercase()) {

            "accepted" -> {
                holder.tvStatus.setTextColor(
                    ContextCompat.getColor(context, R.color.acceptedColor)
                )
                holder.btnPay.visibility = View.VISIBLE
            }

            "paid" -> {
                holder.tvStatus.setTextColor(
                    ContextCompat.getColor(context, R.color.purple)
                )
                holder.btnPay.visibility = View.GONE
            }

            "rejected" -> {
                holder.tvStatus.setTextColor(
                    ContextCompat.getColor(context, R.color.rejectedColor)
                )
                holder.btnPay.visibility = View.GONE
            }

            else -> { // Pending
                holder.tvStatus.setTextColor(
                    ContextCompat.getColor(context, R.color.pendingColor)
                )
                holder.btnPay.visibility = View.GONE
            }
        }

        // Pay button click
        holder.btnPay.setOnClickListener {
            val intent = Intent(context, payment::class.java)

            intent.putExtra("TYPE", "PANDIT")
            intent.putExtra("BOOKING_ID", item.bookingId)
            intent.putExtra("PANDIT_ID", item.panditPushId)

            // ₹1001 → 100100 paise
            intent.putExtra("AMOUNT", amount * 100)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = list.size
}