package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class satyanarayan : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_satyanarayan)

        val bookButton = findViewById<Button>(R.id.btnBookPooja)

        bookButton.setOnClickListener {

            val intent = Intent(this, Search::class.java)
            intent.putExtra("service_name", "Satyanarayan")

            startActivity(intent)
        }
    }
}
