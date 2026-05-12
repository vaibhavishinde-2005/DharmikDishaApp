package com.example.myproject.Fragment

import com.example.myproject.R
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.myproject.*
import com.example.myproject.adapter.PopularAdapter
import com.example.myproject.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val NOTIFICATION_PERMISSION_CODE = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ----------------------------
        // Original code - unchanged
        // ----------------------------
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner3, ScaleTypes.FIT))
        binding.imageSlider.setImageList(imageList, ScaleTypes.FIT)

        binding.imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {}
            override fun onItemSelected(position: Int) {
                Toast.makeText(requireContext(), "Selected Image $position", Toast.LENGTH_SHORT).show()
            }
        })

        val serviceNames =
            listOf("Horoscope", "Aarti", "Pooja", "Sanskar", "Shanti Pooja", "Shraddha", "Yadnya")

        val serviceImages = listOf(
            R.drawable.horoscope,
            R.drawable.aaratiimg,
            R.drawable.satyanarayan,
            R.drawable.sanskar,
            R.drawable.shantipooja,
            R.drawable.shraddha,
            R.drawable.yadnya
        )

        val adapter = PopularAdapter(serviceNames, serviceImages) { position ->
            when (position) {
                0 -> startActivity(android.content.Intent(requireContext(), HoroscopePage::class.java))
                1 -> startActivity(android.content.Intent(requireContext(), AaratiPage::class.java))
                2 -> startActivity(android.content.Intent(requireContext(), satyanarayan::class.java))
                3 -> startActivity(android.content.Intent(requireContext(), Sanskar::class.java))
                4 -> startActivity(android.content.Intent(requireContext(), Shantipooja::class.java))
                5 -> startActivity(android.content.Intent(requireContext(), Shraddha::class.java))
                6 -> startActivity(android.content.Intent(requireContext(), Yadnya::class.java))
                else -> Toast.makeText(requireContext(), "${serviceNames[position]} clicked", Toast.LENGTH_SHORT).show()
            }
        }

        binding.PopularService.layoutManager = LinearLayoutManager(requireContext())
        binding.PopularService.adapter = adapter

        // ----------------------------
        // NEW: Listen for booking acceptance
        // ----------------------------
        requestNotificationPermission()
        listenForBookingAcceptance()
    }

    // ----------------------------
    // Request Notification Permission (for Android 13+)
    // ----------------------------
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_CODE
                )
            }
        }
    }

    // ----------------------------
    // Listen for booking status = "Accepted"
    // ----------------------------
    private fun listenForBookingAcceptance() {
        val userUid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        FirebaseDatabase.getInstance().reference
            .child("bookings")
            .orderByChild("userUid")
            .equalTo(userUid)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val status = snapshot.child("status").value?.toString()
                    if (status == "Accepted") {
                        val panditName = snapshot.child("panditName").value?.toString() ?: "Pandit"
                        val date = snapshot.child("date").value?.toString() ?: ""
                        val time = snapshot.child("time").value?.toString() ?: ""

                        showNotification(
                            "Booking Accepted",
                            "$panditName accepted your booking on $date at $time"
                        )
                    }
                }
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    // ----------------------------
    // Show Local Notification
    // ----------------------------
    private fun showNotification(title: String, message: String) {
        val channelId = "booking_channel"
        val manager = requireContext().getSystemService(android.app.NotificationManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                channelId,
                "Booking Notifications",
                android.app.NotificationManager.IMPORTANCE_HIGH
            )
            manager?.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.panditimg) // put your app's notification icon here
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .build()

        manager?.notify(System.currentTimeMillis().toInt(), notification)
    }
}
