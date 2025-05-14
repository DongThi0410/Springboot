package com.example.androidApp.dtos

import com.example.androidApp.models.SeatType

data class SeatDTO(
    val id: Int,
    val col: Int,
    val state: Int,
    val row: String,
    val type: SeatType,
    val price: Double
)
