package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirmPasswordEditText)
        val signupButton = findViewById<Button>(R.id.signupButton)
        val loginRedirect = findViewById<TextView>(R.id.loginRedirect)

        signupButton.setOnClickListener {

            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            when {
                name.isEmpty() -> nameEditText.error = "Enter your name"

                email.isEmpty() -> emailEditText.error = "Enter email"

                !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                    emailEditText.error = "Enter valid email"

                password.isEmpty() ->
                    passwordEditText.error = "Enter password"

                password.length < 6 ->
                    passwordEditText.error = "Password must be at least 6 characters"

                !isValidPassword(password) ->
                    passwordEditText.error =
                        "Password must contain uppercase, lowercase, number & special character"

                confirmPassword.isEmpty() ->
                    confirmPasswordEditText.error = "Confirm password"

                password != confirmPassword ->
                    confirmPasswordEditText.error = "Passwords do not match"

                else -> createUser(name, email, password)
            }
        }

        loginRedirect.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val pattern =
            Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!]).{6,}$")
        return pattern.matches(password)
    }

    private fun createUser(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {

                val uid = auth.currentUser!!.uid

                val userMap = hashMapOf(
                    "uid" to uid,
                    "name" to name,
                    "email" to email,
                    "createdAt" to System.currentTimeMillis()
                )

                database.child("users").child(uid).setValue(userMap)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Signup Successful. Please login.", Toast.LENGTH_SHORT).show()

                        // 🔁 Redirect to Login page
                        startActivity(Intent(this, Login::class.java))
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
    }
}
