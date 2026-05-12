package com.example.myproject.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproject.adapter.MyBookingAdapter
import com.example.myproject.data.model.BookingPandit
import com.example.myproject.databinding.FragmentMyBookingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MyBookingFragment : Fragment() {

    private lateinit var binding: FragmentMyBookingBinding
    private val bookingList = mutableListOf<BookingPandit>()
    private lateinit var adapter: MyBookingAdapter

    // Track bookings that already showed dialog
    private val shownDialogs = mutableSetOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMyBookingBinding.inflate(inflater, container, false)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) return binding.root

        // ✅ Initialize adapter
        adapter = MyBookingAdapter(requireContext(), bookingList)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // 🔥 Firebase reference to user's bookings
        val bookingRef = FirebaseDatabase.getInstance()
            .getReference("bookings")
            .child(user.uid)

        bookingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                bookingList.clear()   // 🔥 FIX

                for (child in snapshot.children) {

                    val booking = child.getValue(BookingPandit::class.java) ?: continue

                    bookingList.add(booking)
                }

                adapter.notifyDataSetChanged()

                Log.d("MY_BOOKING", "Bookings count = ${bookingList.size}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MY_BOOKING", "Firebase error: ${error.message}")
            }
        })



        return binding.root
    }

    // Show dialog safely
    private fun showStatusDialog(booking: BookingPandit) {
        if (!isAdded) return

        AlertDialog.Builder(requireContext())
            .setTitle("Booking Update")
            .setMessage("Your booking with ${booking.panditName.ifEmpty { "Pandit" }} is ${booking.status.ifEmpty { "Pending" }}.")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .show()
    }
}
