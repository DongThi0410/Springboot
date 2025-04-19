package com.example.androidApp.models

import jakarta.persistence.*

@Entity
@Table(name = "auditorium")
data class Auditorium(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0,

    val name: String
)

