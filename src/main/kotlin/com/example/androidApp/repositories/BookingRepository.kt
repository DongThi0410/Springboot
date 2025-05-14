package com.example.androidApp.repositories

import com.example.androidApp.models.Booking
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository

interface BookingRepository : JpaRepository<Booking, Int> {
    fun findByStatusAndExpiresAtBefore(state: Int, expire: LocalDateTime): List<Booking>
    fun findByStatus(status: Int): List<Booking>
    @Query(
        """SELECT DATE(b.createdAt) as date, SUM(b.total) as total 
            FROM Booking b
            WHERE b.status = 1 AND b.createdAt BETWEEN :start AND :end
            GROUP BY DATE(b.createdAt)
            ORDER BY DATE(b.createdAt) ASC 
         """
    )
    fun getRevenueByDay(
        @Param("start") start: LocalDateTime,
        @Param("end") end: LocalDateTime,

    ): List<Array<Any>>
}
data class RevenueDTO(
    val date: String,
    val total: Double
)