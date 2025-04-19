package com.example.androidApp.models

import jakarta.persistence.*

@Entity
@Table(name="seat_type")
data class SeatType (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int,

    @Column(name = "type_name", nullable = false)
    val typeName: String,
    @Column(name = "price", nullable = false)
    val price: Double,
    val description: String?
)