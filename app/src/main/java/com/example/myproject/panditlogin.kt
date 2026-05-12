package com.example.myproject

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.data.model.Pandit
import com.example.myproject.databinding.ActivityPanditloginBinding
import com.google.firebase.database.FirebaseDatabase

class panditlogin : AppCompatActivity() {

    private lateinit var binding: ActivityPanditloginBinding
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPanditloginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // History icon click (Toolbar)
        binding.topToolbar.findViewById<android.widget.ImageView>(R.id.ivHistory)
            .setOnClickListener {
                // Open History screen
                startActivity(
                    android.content.Intent(
                        this,
                        PanditHistoryActivity::class.java
                    )
                )
            }

        val prefs = getSharedPreferences("PanditPrefs", MODE_PRIVATE)
        val isFirstTime = prefs.getBoolean("isHistoryHintShown", false)

        if (!isFirstTime) {

            AlertDialog.Builder(this)
                .setTitle("Booking History")
                .setMessage("Tap the History icon at the top right to view your past bookings.")
                .setPositiveButton("Got it") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()

            prefs.edit().putBoolean("isHistoryHintShown", true).apply()
        }

        // Image picker (content:// format only)
        val imagePicker =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                if (uri != null) {
                    selectedImageUri = uri
                    binding.edtPanditFile.setText(uri.toString()) // EXACT FORMAT
                }
            }

        binding.btnBrowsePandit.setOnClickListener {
            imagePicker.launch("image/*")
        }

        binding.btnPanditSave.setOnClickListener {
            savePandit()
        }
    }

    private fun savePandit() {

        val name = binding.edtPanditName.text.toString().trim()
        val email = binding.edtPanditEmail.text.toString().trim()
        val phone = binding.edtPanditPhone.text.toString().trim()
        val experience = binding.edtPanditExperience.text.toString().trim()
        val speciality = binding.edtPanditSpeciality.text.toString().trim()
        val location = binding.edtPanditAddress.text.toString().trim()
        val dakshina = binding.edtPanditDakshina.text.toString().trim()

        // ================= VALIDATIONS =================

        if (name.isEmpty()) {
            binding.edtPanditName.error = "Enter name"
            return
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtPanditEmail.error = "Enter valid email"
            return
        }

        // ✅ PHONE VALIDATION
        if (!phone.matches(Regex("^[0-9]{10}$"))) {
            binding.edtPanditPhone.error = "Enter valid 10-digit number"
            return
        }

        if (experience.isEmpty()) {
            binding.edtPanditExperience.error = "Enter experience"
            return
        }

        if (speciality.isEmpty()) {
            binding.edtPanditSpeciality.error = "Enter speciality"
            return
        }

        if (location.isEmpty()) {
            binding.edtPanditAddress.error = "Enter address"
            return
        }

        // ✅ DAKSHINA VALIDATION (only numbers)
        if (!dakshina.matches(Regex("^[0-9]+$"))) {
            binding.edtPanditDakshina.error = "Enter valid amount"
            return
        }

        if (selectedImageUri == null) {
            Toast.makeText(this, "Select image", Toast.LENGTH_SHORT).show()
            return
        }

        // ================= SAVE =================

        val pandit = Pandit(
            name = name,
            email = email,
            phone = phone,
            experience = experience,
            speciality = speciality,
            location = location,
            dakshina = dakshina,
            imageUri = selectedImageUri!!.toString()
        )

        FirebaseDatabase.getInstance()
            .getReference("pandits")
            .push()
            .setValue(pandit)
            .addOnSuccessListener {
                Toast.makeText(this, "Pandit saved successfully", Toast.LENGTH_SHORT).show()
                clearFields()
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearFields() {
        binding.edtPanditName.text.clear()
        binding.edtPanditEmail.text.clear()
        binding.edtPanditPhone.text.clear()
        binding.edtPanditExperience.text.clear()
        binding.edtPanditSpeciality.text.clear()
        binding.edtPanditAddress.text.clear()
        binding.edtPanditDakshina.text.clear()
        binding.edtPanditFile.text.clear()
        selectedImageUri = null
    }
}
