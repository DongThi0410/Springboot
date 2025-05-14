package com.example.androidApp.controllers.user

import com.example.androidApp.dtos.AllSeatHeld
import com.example.androidApp.dtos.GetSeatRequest
import com.example.androidApp.dtos.HoldSeatReq
import com.example.androidApp.dtos.SeatDTO
import com.example.androidApp.models.Seat
import com.example.androidApp.services.SeatService
import com.example.app02.dto.SeatBookingRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user/seat")
class SeatController(private val seatService: SeatService) {
    @GetMapping("/get")
    fun getSeatByShowtime(
        @RequestParam showtimeId: Int,
        @RequestParam userId: Int
    ): List<SeatDTO> {
        return seatService.getSeatsByShowtime(showtimeId, userId)
    }


    @PostMapping("/hold")
    fun holdSeat(@RequestBody request: HoldSeatReq): ResponseEntity<String> {
        return if (seatService.holdSeat(request)) {
            ResponseEntity.ok("Đã giữ ghế")
        } else {
            ResponseEntity.badRequest().body("ghe dang duoc giu boi nguoi khac")
        }
    }

    @PostMapping("/cancel-hold")
    fun cancelHoldSeat(@RequestBody request: HoldSeatReq): ResponseEntity<String> {
        return if (seatService.cancelHold(request)) {
            ResponseEntity.ok("Đã hủy giữ ghế")
        } else {
            ResponseEntity.badRequest().body("ghế này không do bạn giữ")
        }
    }
//    @PostMapping("/cancel-all")
//    fun cancelAllHold(@RequestBody request: AllSeatHeld): ResponseEntity<String>{
//        return if (seatService.cancelAllHold(request)){
//            ResponseEntity.ok("Đã hủy các ghế bạn đã giữ")
//        }else {
//            ResponseEntity.badRequest().body("Ban khong giữ bất kỳ ghế nào")
//        }
//    }
}
