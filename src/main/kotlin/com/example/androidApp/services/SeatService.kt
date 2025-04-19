package com.example.androidApp.services

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
import java.time.LocalDateTime
import kotlin.reflect.jvm.internal.impl.descriptors.deserialization.PlatformDependentDeclarationFilter.All

@Service
class SeatService(
    private val seatRepository: SeatRepository,
    private val showtimeRepository: ShowtimeRepository,
    private val userRepository: UserRepository,
    private val seatHoldRepo: SeatHoldRepo,
    private val bookingSeatRepository: BookingSeatRepository
) {
    fun getSeatsByShowtime(showtimeId: Int, userId: Int): List<SeatDTO> {
        val showtime = showtimeRepository.findById(showtimeId)
            .orElseThrow { RuntimeException("Suất chiếu không tồn tại") }

        val seats = seatRepository.findByAuditoriumId(showtime.auditorium.id)

        val bookingSeats = bookingSeatRepository.findByShowtimeId(showtimeId)
            .map { it.seat.id }
            .toSet()
        val heldSeats = seatHoldRepo.findSeatHoldByShowtimeId(showtimeId)
            .filterNot { it.isExpired() }
            .associateBy { it.seat.id }

        return seats.map { seat ->
            val isHeld = bookingSeats.contains(seat.id)
            val hold = heldSeats[seat.id]
            val state = when {
                isHeld -> 3
                hold == null -> 0
                hold.userId == userId -> 2
                else -> 1
            }
            SeatDTO(
                id = seat.id,
                row = seat.row,
                col = seat.col,
                isReserved = seat.id in bookingSeats,
                type = seat.seatType,
                price = seat.seatType.price,
                state = state

            )
        }
    }



    private fun updateSeatStatusInFirebase(seatId: Int, isReserved: Boolean) {
        val database = FirebaseDatabase.getInstance()
        val seatRef = database.getReference("seats/$seatId")
        seatRef.child("isReserved").setValue(isReserved, null)
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
        val currentHold = seatHoldRepo.findSeatHoldBySeatIdAndShowtimeId(request.seatId, request.showtimeId)
        if (currentHold != null && !currentHold.isExpired()) return false
        currentHold?.let { seatHoldRepo.delete(it) }
        val newHold = SeatHold(
            seat = seat,
            showtime = showtime,
            userId = request.userId,
            holdTime = LocalDateTime.now().plusMinutes(1)
        )
        seatHoldRepo.save(newHold)
        seat.state = SeatState.HELD.code
        seatRepository.save(seat)
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
