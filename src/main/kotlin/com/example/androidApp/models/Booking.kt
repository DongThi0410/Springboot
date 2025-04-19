package com.example.androidApp.models

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime


@Entity
@Table(name = "booking")
data class Booking(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    val showtime: Showtime,
    @OneToMany(mappedBy = "booking", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val bookingSeats: List<BookingSeat> = mutableListOf(),

    val total: Double,
    var status: Int = 0,
    val qrUrl: String,

    @CreationTimestamp
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "expires_at")
    val expiresAt: LocalDateTime? = null
)