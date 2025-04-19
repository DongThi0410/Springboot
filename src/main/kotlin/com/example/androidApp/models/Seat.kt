package com.example.androidApp.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "seat")
data class Seat(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(name = "row", nullable = false)
    val row: String,
    @Column(name = "col", nullable = false)
    val col: Int,

    @ManyToOne
    @JoinColumn(name = "auditorium_id", nullable = false)
    val auditorium: Auditorium,

    @ManyToOne
    @JoinColumn(name = "seat_type_id", nullable = false)
    val seatType: SeatType,

    @Column(name = "state", nullable = false)
    var state: Int = 0,

    )