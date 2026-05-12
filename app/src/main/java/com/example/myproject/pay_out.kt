package com.example.myproject

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.data.model.BookingPandit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class pay_out : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etAddress: EditText
    private lateinit var etDate: EditText
    private lateinit var etTime: EditText
    private lateinit var etPhone: EditText
    private lateinit var buttonProceed: Button

    private lateinit var auth: FirebaseAuth

    private var panditPushId: String = ""
    private var panditName = ""
    private var panditService = ""
    private var panditDakshina = ""

    private var itemName: String? = null
    private var itemPrice: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        panditPushId = intent.getStringExtra("panditPushId") ?: ""
        itemName = intent.getStringExtra("itemName")
        itemPrice = intent.getStringExtra("itemPrice")

        // ===================== PANDIT BOOKING =====================
        if (panditPushId.isNotEmpty()) {

            setContentView(R.layout.activity_pay_out)

            etName = findViewById(R.id.etName)
            etAddress = findViewById(R.id.etAddress)
            etDate = findViewById(R.id.etDate)
            etTime = findViewById(R.id.etTime)
            etPhone = findViewById(R.id.etPhone)
            buttonProceed = findViewById(R.id.buttonProceed)

            etPhone.inputType = android.text.InputType.TYPE_CLASS_NUMBER
            etPhone.filters = arrayOf(InputFilter.LengthFilter(10))

            etName.setText(auth.currentUser?.displayName ?: "")

            etDate.setOnClickListener { showDatePicker() }
            etTime.setOnClickListener { showTimePicker() }

            fetchPanditDetails()

            buttonProceed.setOnClickListener {
                saveBooking()
            }



        }

        // ===================== ITEM PAYMENT =====================
        else {

            setContentView(R.layout.item_payment)

            val tvItemName = findViewById<TextView>(R.id.tvItemName)
            val tvItemPrice = findViewById<TextView>(R.id.tvItemPrice)
            val etQuantity = findViewById<EditText>(R.id.etQuantity)
            val etAddress = findViewById<EditText>(R.id.etAddress)
            val etPhone = findViewById<EditText>(R.id.etPhone)
            val btnPayNow = findViewById<Button>(R.id.btnPayNow)

            tvItemName.text = itemName
            tvItemPrice.text = itemPrice

            etPhone.inputType = android.text.InputType.TYPE_CLASS_NUMBER
            etPhone.filters = arrayOf(InputFilter.LengthFilter(10))

            btnPayNow.setOnClickListener {

                val qtyStr = etQuantity.text.toString()
                val phone = etPhone.text.toString()
                val address = etAddress.text.toString()

                if (qtyStr.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                    Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (!phone.matches(Regex("^[0-9]{10}$"))) {
                    etPhone.error = "Enter valid 10-digit number"
                    return@setOnClickListener
                }

                val cleanPrice = itemPrice
                    ?.replace("₹", "")
                    ?.trim()

                val price = cleanPrice?.toIntOrNull() ?: 0
                val qty = qtyStr.toIntOrNull() ?: 1

                val totalRupees = price * qty
                val totalAmount = totalRupees * 100   // ✅ paise

                val intent = Intent(this, payment::class.java)
                intent.putExtra("TYPE", "ITEM")
                intent.putExtra("ITEM_NAME", itemName)
                intent.putExtra("QUANTITY", qty.toString())
                intent.putExtra("AMOUNT", totalAmount)

                startActivity(intent)
            }
        }
    }

    // ===================== FETCH PANDIT =====================
    private fun fetchPanditDetails() {

        val ref = FirebaseDatabase.getInstance()
            .getReference("pandits")
            .child(panditPushId)

        ref.get().addOnSuccessListener {

            panditName = it.child("name").value?.toString() ?: ""
            panditService = it.child("speciality").value?.toString() ?: ""
            panditDakshina = it.child("dakshina").value?.toString() ?: ""
        }
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, y, m, d ->
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val selected = Calendar.getInstance()
                selected.set(y, m, d)
                etDate.setText(sdf.format(selected.time))
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker() {
        val cal = Calendar.getInstance()
        TimePickerDialog(
            this,
            { _, h, m ->
                etTime.setText(String.format("%02d:%02d", h, m))
            },
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun saveBooking() {

        val name = etName.text.toString()
        val address = etAddress.text.toString()
        val date = etDate.text.toString()
        val time = etTime.text.toString()
        val phone = etPhone.text.toString()

        val user = auth.currentUser ?: return

        if (name.isEmpty() || address.isEmpty() || date.isEmpty() || time.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (!phone.matches(Regex("^[0-9]{10}$"))) {
            etPhone.error = "Enter valid 10-digit number"
            return
        }

        val ref = FirebaseDatabase.getInstance()
            .getReference("bookings")
            .child(user.uid)
            .push()

        val booking = BookingPandit(
            bookingId = ref.key!!,
            customerUid = user.uid,
            customerName = name,
            customerEmail = user.email ?: "",
            panditPushId = panditPushId,
            panditName = panditName,
            panditService = panditService,
            dakshina = panditDakshina,
            address = address,
            phone = phone,
            date = date,
            time = time,
            status = "Pending"
        )

        ref.setValue(booking)
            .addOnSuccessListener {
                AlertDialog.Builder(this)
                    .setTitle("Booking Sent")
                    .setMessage("Your booking request has been sent successfully.")
                    .setPositiveButton("OK") { d, _ ->
                        d.dismiss()
                        finish()
                    }
                    .show()
            }
    }
}