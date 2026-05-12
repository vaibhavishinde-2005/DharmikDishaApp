package com.example.myproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page1) // ✅ layout with NavHost + BottomNav

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navigation)
                    as NavHostFragment

        val navController = navHostFragment.navController

        // 🔥 THIS connects BottomNav with fragments
        bottomNav.setupWithNavController(navController)
    }
}
