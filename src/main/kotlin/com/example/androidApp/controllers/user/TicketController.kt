package com.example.androidApp.controllers.user

import com.example.androidApp.services.BookingService
import com.example.androidApp.services.TicketDTO
import com.example.androidApp.services.TicketService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("user")
class TicketController(
    private val ticketService: TicketService,
){
    @GetMapping("/ticket/{id}")
     fun getTicket(@PathVariable("id")bookingId: Int): ResponseEntity<Any>{
        return try {
            val ticket = ticketService.getTicket(bookingId)
            ResponseEntity.ok(ticket)
        }catch (e: Exception){
            ResponseEntity.badRequest().body(e)
        }
    }
    @GetMapping("/tickets")
    fun getAllTicketByUser(@RequestParam("id") id: Int): ResponseEntity<Any>{
        val tickets = ticketService.getAllTicket(id)
        return if (tickets.isNotEmpty()){
            ResponseEntity.ok(tickets)
        }else
            ResponseEntity.badRequest().body("không tìm thấy ticket nào của ngườu dùng trên")
    }
}