package com.example.myproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout


class AaratiPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aarati_page)   // Replace with your XML file name

        // Aarti block click listeners
        findViewById<LinearLayout>(R.id.blockGanesh).setOnClickListener {
            startActivity(Intent(this, Ganpati::class.java))
        }

        findViewById<LinearLayout>(R.id.blockLakshmi).setOnClickListener {
            startActivity(Intent(this, Lakshmi::class.java))
        }

        findViewById<LinearLayout>(R.id.blockShiv).setOnClickListener {
            startActivity(Intent(this, Shiv::class.java))
        }

        findViewById<LinearLayout>(R.id.blockHnuman).setOnClickListener {
            startActivity(Intent(this, hanuman::class.java))
        }


        findViewById<LinearLayout>(R.id.blockKrishan).setOnClickListener {
          startActivity(Intent(this, krishan::class.java))
        }

        findViewById<LinearLayout>(R.id.blockRam).setOnClickListener {
            startActivity(Intent(this, ram::class.java))
        }

        findViewById<LinearLayout>(R.id.blockSatyanarayan).setOnClickListener {
            startActivity(Intent(this, satyanarayan_aarati::class.java))
        }

        findViewById<LinearLayout>(R.id.blockShani).setOnClickListener {
          startActivity(Intent(this, Shani::class.java))
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
