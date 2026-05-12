package com.example.myproject

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class BookingAdapter(
    private val bookings: MutableList<Booking>,
    private val context: Context
) : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCustomerName: TextView = itemView.findViewById(R.id.tvCustomerName)
        val tvCustomerPhone: TextView = itemView.findViewById(R.id.tvCustomerPhone)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val btnAccept: Button = itemView.findViewById(R.id.btnAccept)
        val btnReject: Button = itemView.findViewById(R.id.btnReject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]

        // Basic data
        holder.tvCustomerName.text = booking.customerName.ifEmpty { "Unknown" }
        holder.tvCustomerPhone.text = booking.phone.ifEmpty { "-" }
        holder.tvDate.text = booking.date.ifEmpty { "-" }
        holder.tvTime.text = booking.time.ifEmpty { "-" }
        holder.tvAddress.text = booking.address.ifEmpty { "-" }

        // Safe status
        val status = if (booking.status.isEmpty()) "Pending" else booking.status
        holder.tvStatus.text = status

        // Status color (case safe)
        when (status.lowercase()) {
            "accepted" -> holder.tvStatus.setTextColor(
                ContextCompat.getColor(context, R.color.acceptedColor)
            )

            "rejected" -> holder.tvStatus.setTextColor(
                ContextCompat.getColor(context, R.color.rejectedColor)
            )

            "paid" -> holder.tvStatus.setTextColor(
                ContextCompat.getColor(context, R.color.purple)
            )

            else -> holder.tvStatus.setTextColor(
                ContextCompat.getColor(context, R.color.pendingColor)
            )
        }

        // Button visibility logic
        when (status.lowercase()) {
            "pending" -> {
                holder.btnAccept.visibility = View.VISIBLE
                holder.btnReject.visibility = View.VISIBLE
            }
            else -> {
                holder.btnAccept.visibility = View.GONE
                holder.btnReject.visibility = View.GONE
            }
        }

        // Click listeners
        holder.btnAccept.setOnClickListener {
            holder.btnAccept.isEnabled = false
            holder.btnReject.isEnabled = false
            updateBookingStatus(booking, "Accepted", position)
        }

        holder.btnReject.setOnClickListener {
            holder.btnAccept.isEnabled = false
            holder.btnReject.isEnabled = false
            updateBookingStatus(booking, "Rejected", position)
        }
    }

    override fun getItemCount(): Int = bookings.size

    private fun updateBookingStatus(booking: Booking, status: String, position: Int) {

        Log.d(
            "BookingAdapter",
            "bookingId=${booking.bookingId}, customerUid=${booking.customerUid}, panditPushId=${booking.panditPushId}"
        )

        if (booking.customerUid.isEmpty() ||
            booking.bookingId.isEmpty() ||
            booking.panditPushId.isEmpty()
        ) {
            Toast.makeText(context, "Invalid booking data", Toast.LENGTH_SHORT).show()
            return
        }

        val database = FirebaseDatabase.getInstance()

        val updateMap = mapOf("status" to status)

        val userRef = database.getReference("bookings")
            .child(booking.customerUid)
            .child(booking.bookingId)

        val panditRef = database.getReference("panditBookings")
            .child(booking.panditPushId)
            .child(booking.bookingId)

        userRef.updateChildren(updateMap).addOnSuccessListener {
            panditRef.updateChildren(updateMap).addOnSuccessListener {

                // Update UI instantly
                booking.status = status
                notifyItemChanged(position)

                Toast.makeText(context, "Booking $status", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {
                Toast.makeText(context, "Failed (Pandit side)", Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener {
            Toast.makeText(context, "Failed (User side)", Toast.LENGTH_SHORT).show()
        }
    }

    fun setBookings(newList: List<Booking>) {
        bookings.clear()
        bookings.addAll(newList)
        notifyDataSetChanged()
    }
}