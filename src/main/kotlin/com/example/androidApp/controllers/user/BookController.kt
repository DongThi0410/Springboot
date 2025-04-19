package com.example.androidApp.controllers.user

import com.example.androidApp.dtos.BookingRequest
import com.example.androidApp.dtos.BookingResponse
import com.example.androidApp.models.Booking
import com.example.androidApp.repositories.BookingRepository
import com.example.androidApp.services.BookingService
import com.example.app02.dto.SeatBookingRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.io.encoding.Base64

@RestController
@RequestMapping("user")
class BookingController(
    private val bookingService: BookingService
) {
    @PostMapping("/book")
    fun bookSeat(@RequestBody request: BookingRequest): BookingResponse{
        return bookingService.createBooking(request)
    }
}