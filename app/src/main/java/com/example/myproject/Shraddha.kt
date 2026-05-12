package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.R.id.btnBookShraddha

class Shraddha : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shraddha)

        val bookButton = findViewById<Button>(btnBookShraddha)

        bookButton.setOnClickListener {

            val intent = Intent(this, Search::class.java)
            intent.putExtra("service_name", "Satyanarayan")

            startActivity(intent)
        }
    }
}
