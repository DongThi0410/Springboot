package com.example.androidApp.repositories

import com.example.androidApp.models.SeatHold
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository

interface SeatHoldRepo : JpaRepository<SeatHold, Int> {
    fun findSeatHoldBySeatIdAndShowtimeId(seatId: Int, showtimeId: Int): SeatHold?
    fun findSeatHoldBySeatIdAndShowtimeIdAndUserId(seatId: Int, showtimeId: Int, userId: Int): SeatHold?
    fun findSeatHoldByShowtimeId(showtimeId: Int): List<SeatHold>
}