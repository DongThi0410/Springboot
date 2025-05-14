package com.example.androidApp.repositories

import com.example.androidApp.models.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository : JpaRepository<Ticket, Int> {
    fun findByBookingId(bookingId: Int): Ticket
    fun findByUserId(userId: Int): List<Ticket>
}