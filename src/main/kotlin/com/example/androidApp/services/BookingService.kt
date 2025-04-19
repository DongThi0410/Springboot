package com.example.androidApp.services

import com.example.androidApp.dtos.BookingRequest
import com.example.androidApp.dtos.BookingResponse
import com.example.androidApp.models.Booking
import com.example.androidApp.models.BookingSeat
import com.example.androidApp.repositories.BookingRepository
import com.example.androidApp.repositories.SeatRepository
import com.example.androidApp.repositories.ShowtimeRepository
import com.example.androidApp.repositories.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.net.URLEncoder
import java.time.LocalDateTime

@Service
class BookingService(
    private val bookingRepository: BookingRepository,
    private val userRepository: UserRepository,
    private val showtimeRepository: ShowtimeRepository,
    private val repository: BookingRepository,
    private val seatRepository: SeatRepository
) {
    fun cancelBooking(){
        val now = LocalDateTime.now()
        val expireBooking = bookingRepository.findByStatusAndExpiresAtBefore(0, now)
        expireBooking.forEach { booking ->
            booking.status = -1 //  đã bị huy
        }
        bookingRepository.saveAll(expireBooking)
        println("da huy ${expireBooking.size} ves hết hạn chưa thanh toán")
    }
    fun generateVietQRUrl(total: Double, info: String): String {
        val accNumb = "0934961226"
        val base = "http://img.vietqr.io/image"
        val bankCode = "BIDV"
        val translate = URLEncoder.encode(info, "UTF-8")
        return "$base/$bankCode-$accNumb-print.png?amount=$total&addInfo=$translate"
    }

    fun createBooking(request: BookingRequest): BookingResponse {
        val user = userRepository.findById(request.userId)
            .orElseThrow { Exception("không tìm thấy người dùng") }
        val showtime = showtimeRepository.findById(request.showtimeId)
            .orElseThrow { Exception("không tìm thấy người dùng") }
        val qrUrl = generateVietQRUrl(total = request.total, info = "Ve ghe ${request.seatIds.joinToString(", ")} ")
        val bookingSeats = request.seatIds.map { seatId ->
            val seat = seatRepository.findById(seatId.toInt())
                .orElseThrow { Exception(" không tìm thấy ghế $seatId") }
            BookingSeat(
                seat = seat,
                showtime = showtime,
                booking = null,
                price = seat.seatType.price
            )
        }
        val booking = Booking(
            user = user,
            showtime = showtime,
            status = 0,
            total = request.total,
            qrUrl = qrUrl,
            bookingSeats = bookingSeats,
            expiresAt = LocalDateTime.now().plusMinutes(10)
        )
        bookingSeats.forEach { it.booking = booking }
        val savedBook = bookingRepository.save(booking)
        return BookingResponse("Đã tạo vé thành công", bookingId = savedBook.id, qrUrl = savedBook.qrUrl)
    }
}