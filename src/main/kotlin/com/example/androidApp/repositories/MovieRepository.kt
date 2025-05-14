package com.example.androidApp.repositories

import com.example.androidApp.models.Movie
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository

interface MovieRepository : JpaRepository<Movie, Int> {
    fun getMoviesByCateId(id: Int): List<Movie>
    fun existsMovieByTitle(name: String): Boolean
    fun findByTitleContainingIgnoreCaseOrDirectorContainingIgnoreCaseOrCastContainingIgnoreCase(query: String, query1: String, query2: String): List<Movie>
}

