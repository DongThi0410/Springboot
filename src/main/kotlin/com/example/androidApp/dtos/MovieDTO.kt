package com.example.androidApp.dtos

import java.time.LocalDate

data class MovieDTO(
    val id: Int,
    val title: String,
    val genre: GenreDTO?,
    val category: CategoryDTO?,
    val duration: Int,
    val poster: String,
    val cast: String,
    val rating: Float,
    val director: String,
    val start_date: LocalDate,
    val end_date: LocalDate
)
