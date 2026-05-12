package com.example.myproject.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import com.example.myproject.R
import com.example.myproject.panditlogin
import com.example.myproject.userlogin

class UserFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnLoginUser = view.findViewById<CardView>(R.id.cardLoginUser)
        val btnLoginPandit = view.findViewById<CardView>(R.id.cardLoginPandit)



        btnLoginUser.setOnClickListener {
            val intent = Intent(requireContext(), userlogin::class.java)
            startActivity(intent)
        }

        btnLoginPandit.setOnClickListener {
            val intent = Intent(requireContext(),panditlogin::class.java)
            startActivity(intent)
        }
    }
}
