package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class firstactivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_firstactivity)
        val btnNext = findViewById<Button>(R.id.button)

        btnNext.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()   // Optional: removes this screen from backstack
        }
    }
}


