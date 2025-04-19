package com.example.androidApp.dtos

data class HoldSeatReq (
    val seatId: Int,
    val userId: Int,
    val showtimeId: Int
)