package com.example.androidApp.models

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "ratings")
data class Rating(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @OneToOne
    @JoinColumn(name = "movie_id")
    val movie: Movie,

    @Column(name = "score")
    val score: Int,

    @Column(name = "comment")
    val comment: String? = null,

    val createdAt: LocalDateTime?= LocalDateTime.now()
)