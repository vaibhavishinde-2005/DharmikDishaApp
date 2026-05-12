package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.Fragment.UserFragment
import com.example.myproject.data.model.Customer
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlin.jvm.java

class userlogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.userlogin)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "User Profile"

        val edtFullName = findViewById<EditText>(R.id.etName)
        val edtEmail = findViewById<EditText>(R.id.etEmail)
        val edtPhone = findViewById<EditText>(R.id.etPhone)
        val edtAddress = findViewById<EditText>(R.id.etAddress)
        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {

            val name = edtFullName.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val phone = edtPhone.text.toString().trim()
            val address = edtAddress.text.toString().trim()

            // ================= VALIDATIONS =================

            // Name (only alphabets)
            if (name.isEmpty()) {
                edtFullName.error = "Enter name"
                return@setOnClickListener
            }
            if (!name.matches(Regex("^[a-zA-Z ]+$"))) {
                edtFullName.error = "Only alphabets allowed"
                return@setOnClickListener
            }

            // Email
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmail.error = "Enter valid email"
                return@setOnClickListener
            }

            // Phone (exactly 10 digits)
            if (!phone.matches(Regex("^[0-9]{10}$"))) {
                edtPhone.error = "Enter valid 10-digit number"
                return@setOnClickListener
            }

            // Address
            if (address.isEmpty()) {
                edtAddress.error = "Enter address"
                return@setOnClickListener
            }

            // ================= SAVE DATA =================

            val customer = Customer(name, email, phone, address)

            val database = FirebaseDatabase.getInstance()
            val customersRef = database.getReference("customers")

            val customerId = customersRef.push().key

            if (customerId != null) {
                customersRef.child(customerId).setValue(customer)
                    .addOnSuccessListener {

                        Toast.makeText(this, "Details Saved Successfully!", Toast.LENGTH_LONG).show()

                        val intent = Intent(this, UserFragment::class.java)
                        intent.putExtra("userName", name)
                        intent.putExtra("userEmail", email)
                        intent.putExtra("userPhone", phone)
                        intent.putExtra("userAddress", address)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to save data: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            }
        }




    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_menu, menu)
        return true



    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.menu_orders -> {
                startActivity(Intent(this, MyOrdersActivity::class.java))
                return true
            }

            R.id.menu_feedback -> {
                startActivity(Intent(this, feedback::class.java))
                return true
            }

            R.id.menu_logout -> {
                logoutUser()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logoutUser() {

        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->

                //  logout happens ONLY when user clicks YES
                FirebaseAuth.getInstance().signOut()

                val intent = Intent(this, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

                Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }



}
