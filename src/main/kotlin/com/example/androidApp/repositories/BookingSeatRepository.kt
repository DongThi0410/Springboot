package com.example.androidApp.repositories

import com.example.androidApp.models.BookingSeat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository

interface BookingSeatRepository : JpaRepository<BookingSeat, Int> {
    fun findByShowtimeId(showtimeId: Int): List<BookingSeat>
    fun findByBookingId(bookingId: Int): List<BookingSeat>
    fun findBySeatIdAndShowtimeId(seatId: Int, showtimeId: Int): BookingSeat?
}