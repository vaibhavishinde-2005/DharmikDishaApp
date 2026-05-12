package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Shantipooja : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shantipooja)

        val bookButton = findViewById<Button>(R.id.btnBookShantiPooja)

        bookButton.setOnClickListener {

            val intent = Intent(this, Search::class.java)
            intent.putExtra("service_name", "Satyanarayan")

            startActivity(intent)
        }
    }
}
