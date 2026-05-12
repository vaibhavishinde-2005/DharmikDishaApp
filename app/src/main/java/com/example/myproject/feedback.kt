package com.example.myproject

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class feedback : AppCompatActivity() {

    private lateinit var etFeedback: EditText
    private lateinit var btnSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        etFeedback = findViewById(R.id.etFeedback)
        btnSubmit = findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            saveFeedback()
        }
    }

    private fun saveFeedback() {

        val feedbackText = etFeedback.text.toString()

        if (feedbackText.isEmpty()) {
            Toast.makeText(this, "Enter feedback", Toast.LENGTH_SHORT).show()
            return
        }

        val user = FirebaseAuth.getInstance().currentUser ?: return
        val ref = FirebaseDatabase.getInstance()
            .getReference("feedback")
            .child(user.uid)
            .push()

        val feedbackMap = HashMap<String, Any>()
        feedbackMap["message"] = feedbackText
        feedbackMap["timestamp"] = System.currentTimeMillis()

        ref.setValue(feedbackMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Feedback submitted", Toast.LENGTH_SHORT).show()
                etFeedback.setText("")
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}