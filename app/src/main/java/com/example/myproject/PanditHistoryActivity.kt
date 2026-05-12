package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PanditHistoryActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pandit_history)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter email & password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginPandit(email, password)
        }
    }

    // ================= LOGIN =================
    private fun loginPandit(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                getPanditPushIdByEmail(email) { panditPushId ->
                    fetchBookingsByPanditPushId(panditPushId)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Invalid user", Toast.LENGTH_SHORT).show()
            }
    }

    // ================= GET PANDIT PUSH ID =================
    private fun getPanditPushIdByEmail(email: String, onSuccess: (String) -> Unit) {

        database.child("pandits")
            .orderByChild("email")
            .equalTo(email.trim())
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {

                        val panditSnap = snapshot.children.firstOrNull()

                        if (panditSnap != null) {
                            val pushId = panditSnap.key ?: ""

                            Log.d("PUSH_ID", pushId)
                            onSuccess(pushId)
                        } else {
                            Toast.makeText(
                                this@PanditHistoryActivity,
                                "Pandit not found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else {
                        Toast.makeText(
                            this@PanditHistoryActivity,
                            "Pandit not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@PanditHistoryActivity, error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    // ================= FETCH BOOKINGS =================
    private fun fetchBookingsByPanditPushId(panditPushId: String) {

        val bookingsList = ArrayList<Booking>()

        database.child("bookings")
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    bookingsList.clear()

                    for (userSnap in snapshot.children) {

                        for (bookingSnap in userSnap.children) {

                            val booking = bookingSnap.getValue(Booking::class.java)

                            if (booking == null) continue

                            // Debug check
                            Log.d(
                                "CHECK",
                                "DB: ${booking.panditPushId} | LOGIN: $panditPushId"
                            )

                            // Match pandit
                            if (booking.panditPushId.trim() == panditPushId.trim()) {
                                bookingsList.add(booking)
                            }
                        }
                    }

                    Log.d("TOTAL_MATCH", bookingsList.size.toString())

                    if (bookingsList.isNotEmpty()) {

                        val intent = Intent(
                            this@PanditHistoryActivity,
                            BookingsActivity::class.java
                        )

                        intent.putParcelableArrayListExtra("bookingsList", bookingsList)
                        startActivity(intent)

                    } else {
                        Toast.makeText(
                            this@PanditHistoryActivity,
                            "No bookings found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@PanditHistoryActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}