package com.example.myproject

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.databinding.ActivityAaratiPageBinding
import com.example.myproject.databinding.ActivityGanpatiBinding
import com.example.myproject.databinding.ActivityHanumanBinding

class hanuman : AppCompatActivity() {

    private lateinit var binding: ActivityHanumanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHanumanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ------------------------
        // BACK BUTTON FUNCTION
        // ------------------------
        binding.topToolbar.setNavigationOnClickListener {
            val intent = Intent(this, AaratiPage::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
