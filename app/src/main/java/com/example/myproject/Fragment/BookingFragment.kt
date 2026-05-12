package com.example.myproject.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproject.adapter.CartAdapter
import com.example.myproject.databinding.FragmentBookingBinding
import com.example.myproject.pay_out

class BookingFragment : Fragment() {

    private lateinit var binding: FragmentBookingBinding
    private var panditPushId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBookingBinding.inflate(inflater, container, false)

        val source = arguments?.getString("source") ?: ""

        // Get panditPushId if coming from Pandit page
        if (source == "PANDIT") {
            panditPushId = arguments?.getString("panditPushId") ?: ""
        }

        // Cart lists
        val cartName = mutableListOf<String>()
        val cartService = mutableListOf<String>()
        val cartPrice = mutableListOf<String>()
        val cartImage = mutableListOf<String>()

        // =============================
        // FROM PANDIT PAGE
        // =============================
        if (source == "PANDIT") {

            val panditName = arguments?.getString("panditName") ?: ""
            val panditService = arguments?.getString("panditService") ?: ""
            val panditExperience = arguments?.getString("panditExperience") ?: ""

            cartName.add(panditName)
            cartService.add("$panditService • $panditExperience yrs")
            cartPrice.add("₹1001")
            cartImage.add("panditimg")
        }

        // =============================
        // FROM SEARCH ITEM PAGE
        // =============================
        else if (source == "POOJA_ITEM") {

            val itemName = arguments?.getString("itemName") ?: ""
            val itemDesc = arguments?.getString("itemDesc") ?: ""
            val itemPrice = arguments?.getString("itemPrice") ?: ""
            val itemImageRes = arguments?.getInt("itemImage") ?: 0

            cartName.add(itemName)
            cartService.add(itemDesc)
            cartPrice.add(itemPrice)

            val imageName =
                if (itemImageRes != 0)
                    resources.getResourceEntryName(itemImageRes)
                else
                    "aaratiimg"

            cartImage.add(imageName)
        }

        // =============================
        // RecyclerView Setup
        // =============================
        binding.cartrecyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        binding.cartrecyclerView.adapter =
            CartAdapter(cartName, cartService, cartPrice, cartImage)

        // =============================
        // Proceed Button
        // =============================
        binding.proceedButton.setOnClickListener {

            if (cartName.isEmpty()) {
                Toast.makeText(requireContext(), "Cart is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(requireContext(), pay_out::class.java)

            // If booking a pandit
            if (panditPushId.isNotEmpty()) {
                intent.putExtra("panditPushId", panditPushId)
            }

            // If buying pooja item
            val itemName = arguments?.getString("itemName") ?: ""
            val itemPrice = arguments?.getString("itemPrice") ?: ""

            intent.putExtra("itemName", itemName)
            intent.putExtra("itemPrice", itemPrice)

            startActivity(intent)
        }

        return binding.root
    }
}