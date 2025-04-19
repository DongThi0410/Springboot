package com.example.androidApp.models
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "showtime")
data class Showtime(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    val movie: Movie,

    @ManyToOne
    @JoinColumn(name = "aud_id", nullable = false)
    val auditorium: Auditorium,

    val startTime: LocalDateTime,
    val endTime: LocalDateTime,


)

