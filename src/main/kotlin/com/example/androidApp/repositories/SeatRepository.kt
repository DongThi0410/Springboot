package com.example.androidApp.repositories

import com.example.androidApp.models.Seat
import com.example.androidApp.models.Showtime
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface SeatRepository: JpaRepository<Seat, Int> {
    fun findByAuditoriumId(auditoriumId: Int): List<Seat>
//    fun findSeatByIdAndAndState(id: Long, state: Int): Optional<Seat>
//    fun findAllByState(state: Int): List<Seat>
}