package com.example.androidApp.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "seat_hold")
data class SeatHold(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne
    @JoinColumn(name = "seat_id")
    val seat: Seat,

    @ManyToOne
    @JoinColumn(name = "showtime_id")
    val showtime: Showtime,

    val userId: Int,
    val holdTime: LocalDateTime
)
