package com.example.myproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView

class HoroscopePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horoscope)

        val toolbar = findViewById<Toolbar>(R.id.toolbarHoroscope)
        setSupportActionBar(toolbar)

        // Back Navigation
        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, HomePage1::class.java))
            finish()
        }

        // ⭐ CARD 1 → ARIES PAGE
        val cardAries = findViewById<CardView>(R.id.cardAries)
        cardAries.setOnClickListener {
            val intent = Intent(this, Aries::class.java)
            startActivity(intent)
        }

        // ⭐ CARD 2 → TAURUS PAGE
        val cardTaurus = findViewById<CardView>(R.id.cardTaurus)
        cardTaurus.setOnClickListener {
            val intent = Intent(this, Taurus::class.java)
            startActivity(intent)
        }

        val cardGemini = findViewById<CardView>(R.id.cardGemini)
        cardGemini.setOnClickListener {
            val intent = Intent(this, Gemini::class.java)
            startActivity(intent)
        }

        val cardCancer = findViewById<CardView>(R.id.cardCancer)
        cardCancer.setOnClickListener {
            val intent = Intent(this, Cancer::class.java)
            startActivity(intent)
        }

        val cardLeo = findViewById<CardView>(R.id.cardLeo)
        cardLeo.setOnClickListener {
            val intent = Intent(this, Leo::class.java)
            startActivity(intent)
        }

        val cardVirgo = findViewById<CardView>(R.id.cardVirgo)
        cardVirgo.setOnClickListener {
            val intent = Intent(this, Virgo::class.java)
            startActivity(intent)
        }

        val cardLibra = findViewById<CardView>(R.id.cardLibra)
        cardLibra.setOnClickListener {
            val intent = Intent(this, Libra::class.java)
            startActivity(intent)
        }

        val cardScorpio = findViewById<CardView>(R.id.cardScorpio)
        cardScorpio.setOnClickListener {
            val intent = Intent(this, Scorpio::class.java)
            startActivity(intent)
        }

        val cardSagittarius = findViewById<CardView>(R.id.cardSagittarius)
        cardSagittarius.setOnClickListener {
            val intent = Intent(this, Sagittarius::class.java)
            startActivity(intent)
        }

        val cardCapricorn = findViewById<CardView>(R.id.cardCapricorn)
        cardCapricorn.setOnClickListener {
            val intent = Intent(this, Capricorn::class.java)
            startActivity(intent)
        }

        val cardAquarius = findViewById<CardView>(R.id.cardAquarius)
        cardAquarius.setOnClickListener {
            val intent = Intent(this,Aquarius::class.java)
            startActivity(intent)
        }

        val cardPisces = findViewById<CardView>(R.id.cardPisces)
        cardPisces.setOnClickListener {
            val intent = Intent(this, Pisces::class.java)
            startActivity(intent)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
