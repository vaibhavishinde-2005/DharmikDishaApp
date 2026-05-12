package com.example.myproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class Sagittarius : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sagittarius)

        val toolbar = findViewById<Toolbar>(R.id.toolbarSagittarius)
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
