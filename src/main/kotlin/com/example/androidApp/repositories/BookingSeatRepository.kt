package com.example.androidApp.repositories

import com.example.androidApp.models.BookingSeat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository

interface BookingSeatRepository : JpaRepository<BookingSeat, Int> {
    fun findByShowtimeId(showtimeId: Int): List<BookingSeat>
    fun findByBookingId(bookingId: Int): List<BookingSeat>
    fun findBySeatIdAndShowtimeId(seatId: Int, showtimeId: Int): BookingSeat?

    @Query("""SELECT COUNT (bs) FROM BookingSeat bs JOIN Booking  b ON bs.booking.id = b.id WHERE b.showtime.id = :showtimeId AND b.status = 1 """)
    fun countPaidSeatsByShowtimeId(@Param("showtimeId") showtimeId: Long): Int
}