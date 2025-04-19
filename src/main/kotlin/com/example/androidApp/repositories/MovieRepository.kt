package com.example.androidApp.repositories

import com.example.androidApp.models.Movie
import org.springframework.data.jpa.repository.JpaRepository

interface MovieRepository : JpaRepository<Movie, Int> {
    fun getMoviesByCateId(id: Int): List<Movie>
}

