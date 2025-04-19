package com.example.androidApp.dtos

data class BookingResponse(
    val msg: String,
    val bookingId: Int,
    val qrUrl: String
)