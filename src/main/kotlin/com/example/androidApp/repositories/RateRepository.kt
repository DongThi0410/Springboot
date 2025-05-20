package com.example.androidApp.repositories

import com.example.androidApp.models.Rating
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RateRepository : JpaRepository<Rating, Int> {
    fun findByMovieId(id: Int): List<Rating>

    @Query(value = "SELECT AVG(score) FROM ratings WHERE movie_id = :movieId", nativeQuery = true)
    fun findAverageRatingByMovieId(@Param("movieId") movieId: Int): Double?
}