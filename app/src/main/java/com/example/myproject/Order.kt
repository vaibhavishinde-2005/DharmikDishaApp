package com.example.myproject


data class Order (
        var orderId: String = "",
        var itemName: String = "",
        var quantity: String = "",
        var deliveryDate: String = "",
        var paymentStatus: String = ""
)