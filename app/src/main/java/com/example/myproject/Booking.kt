package com.example.myproject

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Booking(
    var bookingId: String = "",
    var customerUid: String = "",
    var customerName: String = "",
    var customerEmail: String = "",
    var panditPushId: String = "",
    var panditName: String = "",
    var panditService: String = "",
    var dakshina: String = "",
    var address: String = "",
    var phone: String = "",
    var date: String = "",
    var time: String = "",
    var status: String = ""
) : Parcelable