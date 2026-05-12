@file:Suppress("DEPRECATION")

package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signUpText = findViewById<TextView>(R.id.signUpText)
        val googleLoginButton = findViewById<Button>(R.id.googleLoginButton)
        val forgotPasswordText = findViewById<TextView>(R.id.forgotPasswordText)

        // 🔹 Google Sign-In Setup
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleLoginButton.setOnClickListener {
            googleLauncher.launch(googleSignInClient.signInIntent)
        }

        signUpText.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }

        // 🔥 Forgot Password
        forgotPasswordText.setOnClickListener {
            showResetPasswordDialog()
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            when {
                email.isEmpty() -> emailEditText.error = "Enter email"
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                    emailEditText.error = "Enter valid email"
                password.isEmpty() -> passwordEditText.error = "Enter password"
                else -> loginUser(email, password)
            }
        }
    }

    // 🔹 Reset Password Dialog
    private fun showResetPasswordDialog() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Reset Password")

        val input = EditText(this)
        input.hint = "Enter your registered email"
        input.setPadding(40, 20, 40, 20)
        builder.setView(input)

        builder.setPositiveButton("Send") { _, _ ->

            val email = input.text.toString().trim()

            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Enter valid email", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    Toast.makeText(this,
                        "Password reset link sent to your email",
                        Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this,
                        "Email not registered",
                        Toast.LENGTH_LONG).show()
                }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    // 🔹 Google Sign-In Result
    private val googleLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: Exception) {
                Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show()
            }
        }

    // 🔹 Google Login
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                Toast.makeText(this, "Google Login Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomePage1::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Google authentication failed", Toast.LENGTH_LONG).show()
            }
    }

    // 🔹 Email Login
    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomePage1::class.java))
                finish()
            }
            .addOnFailureListener { exception ->

                val message = when (exception) {
                    is FirebaseAuthInvalidUserException ->
                        "User does not exist"

                    is FirebaseAuthInvalidCredentialsException ->
                        "Invalid email or password"

                    else ->
                        "Login failed. Please try again"
                }

                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
    }
}