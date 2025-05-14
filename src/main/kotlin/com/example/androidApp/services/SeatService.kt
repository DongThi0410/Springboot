package com.example.androidApp.services

import com.example.androidApp.config.SeatStatusUpdate
import com.example.androidApp.dtos.AllSeatHeld
import com.example.androidApp.dtos.HoldSeatReq
import com.example.androidApp.dtos.SeatDTO
import com.example.androidApp.models.Seat
import com.example.androidApp.models.SeatHold
import com.example.androidApp.repositories.*
import com.example.app02.dto.SeatBookingRequest
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import jakarta.transaction.Transaction
import org.apache.juli.logging.Log
import org.slf4j.LoggerFactory
import org.springframework.messaging.simp.SimpMessagingTemplate
import java.time.LocalDateTime
import kotlin.reflect.jvm.internal.impl.descriptors.deserialization.PlatformDependentDeclarationFilter.All

@Service
class SeatService(
    private val seatRepository: SeatRepository,
    private val showtimeRepository: ShowtimeRepository,
    private val userRepository: UserRepository,
    private val seatHoldRepo: SeatHoldRepo,
    private val bookingSeatRepository: BookingSeatRepository,
     val messageTemplate: SimpMessagingTemplate
) {
    fun getSeatsByShowtime(showtimeId: Int, userId: Int): List<SeatDTO> {
        val showtime = showtimeRepository.findById(showtimeId)
            .orElseThrow { RuntimeException("Suất chiếu không tồn tại") }

        val seats = seatRepository.findByAuditoriumId(showtime.auditorium.id)

        val bookingSeats = bookingSeatRepository.findByShowtimeId(showtimeId)
            .associateBy { it.seat.id }
        val heldSeats = seatHoldRepo.findSeatHoldByShowtimeId(showtimeId)
            .filterNot { it.isExpired() }
            .associateBy { it.seat.id }

        return seats.map { seat ->
            val bookingSeat= bookingSeats[seat.id]
            val hold = heldSeats[seat.id]
            var state = 0
            var isReserved = false
            if (bookingSeat != null){
                val booking = bookingSeat.booking
                when (booking?.status){
                    1 -> {
                        state = 3 // đã thanh toán
                        isReserved = true
                    }
                    0 -> {
                        state = 1 // người khác đang thanh toán
                    }
                }
            }else if (hold != null){
                state = if (hold.userId == userId) 2 // mình đang giữ
                else 1 // người khác đang giữ
                isReserved = false
            }else state = 0
            SeatDTO(
                id = seat.id,
                row = seat.row,
                col = seat.col,
                type = seat.seatType,
                price = seat.seatType.price,
                state = state

            )
        }
    }




    fun SeatHold.isExpired(): Boolean {
        return this.holdTime?.isBefore(LocalDateTime.now()) ?: true
    }

    @Transactional
    fun holdSeat(request: HoldSeatReq): Boolean {
        val seat = seatRepository.findById(request.seatId)
            .orElseThrow { Exception("seat no exist") }
        val showtime = showtimeRepository.findById(request.showtimeId)
            .orElseThrow { Exception("showtime no exist") }
        val bookingSeat = bookingSeatRepository.findBySeatIdAndShowtimeId(seat.id, showtime.id)
        if (bookingSeat != null) return false

        val currentHold = seatHoldRepo.findSeatHoldBySeatIdAndShowtimeId(request.seatId, request.showtimeId)
        if (currentHold != null && !currentHold.isExpired()) return false
        currentHold?.let { seatHoldRepo.delete(it) }
        val newHold = SeatHold(
            seat = seat,
            showtime = showtime,

            userId = request.userId,
            holdTime = LocalDateTime.now().plusMinutes(3)
        )
        seatHoldRepo.save(newHold)
        seat.state = SeatState.HELD.code
        seatRepository.save(seat)

        val update = SeatStatusUpdate(seat.id, request.showtimeId, SeatState.HELD.code, request.userId)
       messageTemplate.convertAndSend("/topic/seats${request.showtimeId}", update)
        return true
    }

    fun cancelHold(request: HoldSeatReq): Boolean {

        val currHold = seatHoldRepo.findSeatHoldBySeatIdAndShowtimeId(request.seatId, request.showtimeId)
            ?: return false
        val seat = seatRepository.findById(request.seatId)
            .orElseThrow { Exception("Không tồn tại ghế ${request.seatId}") }
        println("🟢 [CancelHold] userId trong request: ${request.userId}")
        println("🟡 [CancelHold] userId đang giữ ghế: ${currHold.userId}")

        return if (currHold.userId == request.userId) {
            seatHoldRepo.delete(currHold)
            seat.state = SeatState.AVAILABLE.code
            seatRepository.save(seat)

            val update = SeatStatusUpdate(seat.id, request.showtimeId, SeatState.AVAILABLE.code, request.userId)
           messageTemplate.convertAndSend("/topic/seats${request.showtimeId}", update)
            println("✅ [CancelHold] Hủy giữ ghế thành công.")
            true
        } else {
            println("❌ [CancelHold] userId không khớp => không thể hủy giữ.")
            false
        }
    }

//    @Transactional

}

//    }
//        return true
//        seatHoldRepo.deleteAll(allSeatHeld)
//        if (allSeatHeld.isEmpty()) return false
//        val allSeatHeld = seatHoldRepo.findSeatHoldByUserId(request.userId, request.showtimeId)
//    fun cancelAllHold(request: AllSeatHeld): Boolean{
enum class SeatState(val code: Int) {
    AVAILABLE(0),
    HELD(1),
    BOOKED(2)
}
