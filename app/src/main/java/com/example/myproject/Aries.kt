package com.example.myproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class Aries : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aries)

        val toolbar = findViewById<Toolbar>(R.id.toolbarAries)
        setSupportActionBar(toolbar)

        // Back arrow click → go to HoroscopePage
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, HoroscopePage::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
