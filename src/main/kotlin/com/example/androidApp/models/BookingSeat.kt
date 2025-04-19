package com.example.androidApp.models

import jakarta.persistence.*

@Entity
@Table(name = "booking_seat")
data class BookingSeat(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    var booking: Booking? = null,

    @OneToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    val showtime: Showtime,
    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = false)
    val seat: Seat,
    @Column
    var price: Double

    )