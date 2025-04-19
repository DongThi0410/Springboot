package com.example.androidApp.repositories

import com.example.androidApp.models.BookingSeat
import org.springframework.data.jpa.repository.JpaRepository

interface BookingSeatRepository : JpaRepository<BookingSeat, Int> {
    fun findByShowtimeId(showtimeId: Int): List<BookingSeat>
}