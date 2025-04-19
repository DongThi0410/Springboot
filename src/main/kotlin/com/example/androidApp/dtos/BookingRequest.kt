package com.example.androidApp.dtos

data class BookingRequest (
    val userId: Int,
    val showtimeId: Int,
    val seatIds: List<String>,
    val total: Double
)