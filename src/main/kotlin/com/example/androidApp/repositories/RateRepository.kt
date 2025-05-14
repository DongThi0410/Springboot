package com.example.androidApp.repositories

import com.example.androidApp.models.Rating
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RateRepository : JpaRepository<Rating, Int> {
    fun findByMovieId(id: Int): List<Rating>
}