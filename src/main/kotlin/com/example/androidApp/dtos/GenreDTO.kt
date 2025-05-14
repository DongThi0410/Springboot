package com.example.androidApp.dtos

import com.example.androidApp.models.Genre

data class GenreDTO(
    val id: Int,
    val name: String
)
fun GenreDTO.toEntity(): Genre{
    val genre = Genre()
    genre.id = this.id
    genre.name = this.name
    return genre
}
