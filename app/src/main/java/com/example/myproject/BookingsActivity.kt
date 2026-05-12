package com.example.myproject

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BookingsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookings)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // ✅ GET FILTERED DATA ONLY
        val bookingsList = intent.getParcelableArrayListExtra<Booking>("bookingsList") ?: arrayListOf()

        Log.d("UI_LIST_SIZE", bookingsList.size.toString())

        // ✅ SET DATA (NO FIREBASE CALL HERE)
        adapter = BookingAdapter(bookingsList.toMutableList(), this)
        recyclerView.adapter = adapter

        if (bookingsList.isEmpty()) {
            Toast.makeText(this, "No bookings found", Toast.LENGTH_SHORT).show()
        }
    }
}