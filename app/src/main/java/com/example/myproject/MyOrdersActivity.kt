package com.example.myproject

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.adapter.MyOrdersAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MyOrdersActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvNoOrders: TextView
    private lateinit var list: ArrayList<Order>
    private lateinit var adapter:MyOrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_orders)

        recyclerView = findViewById(R.id.recyclerViewOrders)
        tvNoOrders = findViewById(R.id.tvNoOrders)

        recyclerView.layoutManager = LinearLayoutManager(this)

        list = ArrayList()
        adapter = MyOrdersAdapter(list)
        recyclerView.adapter = adapter

        loadOrders()
    }

    private fun loadOrders() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        FirebaseDatabase.getInstance().reference
            .child("orders")
            .child(uid)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()

                    if (!snapshot.exists()) {
                        tvNoOrders.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        tvNoOrders.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE

                        for (data in snapshot.children) {
                            val order = data.getValue(Order::class.java)
                            order?.let { list.add(it) }
                        }
                    }

                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}