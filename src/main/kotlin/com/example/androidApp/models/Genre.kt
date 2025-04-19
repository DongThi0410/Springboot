package com.example.androidApp.models

import jakarta.persistence.*

@Entity
@Table(name = "genres")
class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0

    @Column(nullable = false, length = 255)
    var name: String = ""
}
