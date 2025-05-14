package com.example.androidApp.dtos

import java.time.LocalDate

data class MovieDTO(
    val id: Int,
    val des: String,
    val title: String,
    val trailer: String,
    val genre: GenreDTO?,
    val category: CategoryDTO?,
    val duration: Int,
    val poster: String,
    val cast: String,
    val rating: Float,
    val director: String,
    val startDate: LocalDate,
    val endDate: LocalDate
)
data class MovieUpdateDTO(
    val des: String? = null,
    val title: String? = null,
//    val genreId: Int? = null,
    val duration: Int? = null,
    val poster: String? = null,
    val cast: String? = null,
    val director: String? = null,
)
