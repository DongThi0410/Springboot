package com.example.androidApp.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "verification_token")
data class VerificationToken(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(nullable = false, unique = true)
    val token: String,
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(name = "expiry_date", nullable = false)
    val expiryDate: LocalDateTime

)
