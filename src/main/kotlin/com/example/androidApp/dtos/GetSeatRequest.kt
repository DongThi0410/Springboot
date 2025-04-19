package com.example.androidApp.dtos

import com.example.androidApp.models.SeatType

data class GetSeatRequest(
    val showtimeId: Int,
    val userId: Int,

)
