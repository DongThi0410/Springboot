package com.example.androidApp.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "ticket")
data class Ticket(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @OneToOne
    @JoinColumn(name = "booking_id", unique = true)
    val booking: Booking,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    val used: Boolean = false,
    @Column(name = "issued_at")
    val issuedAt: LocalDateTime,
    @Column(name = "qr_url")
    val qrUrl: String
)