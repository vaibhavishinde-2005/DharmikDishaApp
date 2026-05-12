package com.example.myproject

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.Fragment.BookingFragment
import com.example.myproject.adapter.ServiceAdapter
import com.example.myproject.data.model.Pandit
import com.google.firebase.database.FirebaseDatabase

class Search : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var edtLocation: EditText
    private lateinit var adapter: ServiceAdapter

    private val mList = ArrayList<Service1Data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)
        edtLocation = findViewById(R.id.edtLocation)

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ServiceAdapter(mList) { selectedItem ->
            openBookingFragment(selectedItem)
        }
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText, edtLocation.text.toString())
                return true
            }
        })

        edtLocation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterList(searchView.query.toString(), s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        fetchDataFromFirebase()
    }

    private fun filterList(query: String?, location: String?) {
        val filteredList = ArrayList<Service1Data>()

        for (item in mList) {
            val matchesQuery = query.isNullOrBlank() ||
                    item.title.contains(query!!, true) ||
                    item.service.contains(query, true)

            val matchesLocation = location.isNullOrBlank() ||
                    item.location.contains(location!!, true)

            if (matchesQuery && matchesLocation) {
                filteredList.add(item)
            }
        }

        adapter.setFilterList(filteredList)
    }

    private fun fetchDataFromFirebase() {
        FirebaseDatabase.getInstance().getReference("pandits")
            .get()
            .addOnSuccessListener { snapshot ->
                mList.clear()
                for (child in snapshot.children) {
                    val pandit = child.getValue(Pandit::class.java)
                    pandit?.let {
                        mList.add(
                            Service1Data(
                                panditPushId = child.key ?: "",
                                title = it.name,
                                logo = R.drawable.panditimg,
                                service = it.speciality,
                                experience = it.experience,
                                location = it.location
                            )
                        )
                    }
                }
                adapter.setFilterList(mList)
            }
    }

    // 🔥 OPEN BOOKING FRAGMENT (FIXED)
    private fun openBookingFragment(item: Service1Data) {

        val fragment = BookingFragment().apply {
            arguments = Bundle().apply {
                putString("source", "PANDIT")
                putString("panditName", item.title)
                putString("panditPushId", item.panditPushId)
                putString("panditService", item.service)
                putString("panditExperience", item.experience)
            }
        }

        findViewById<View>(R.id.search_content).visibility = View.GONE
        findViewById<View>(R.id.fragment_container).visibility = View.VISIBLE

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    // 🔙 BACK HANDLING (FIXED)
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            findViewById<View>(R.id.fragment_container).visibility = View.GONE
            findViewById<View>(R.id.search_content).visibility = View.VISIBLE
        } else {
            super.onBackPressed()
        }
    }
}
