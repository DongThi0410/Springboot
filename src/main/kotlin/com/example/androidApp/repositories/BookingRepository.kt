package com.example.androidApp.repositories

import com.example.androidApp.models.Booking
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
@Repository

interface BookingRepository : JpaRepository<Booking, Int> {
    fun findByStatusAndExpiresAtBefore(state: Int, expire: LocalDateTime): List<Booking>
}