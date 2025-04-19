package com.example.androidApp.controllers

import com.example.androidApp.services.ShowtimeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@RestController
@RequestMapping("public")
class SchedulingController(private val showtimeService: ShowtimeService) {

    @PostMapping("/schedule")
    fun schedule(@RequestParam startTime: String, @RequestParam endTime: String): ResponseEntity<String> {
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
            val parsedStart = LocalDateTime.parse(startTime, formatter)
            val parsedEnd = LocalDateTime.parse(endTime, formatter)

            showtimeService.scheduleShowtimes(parsedStart, parsedEnd)
            ResponseEntity.ok("Lịch chiếu đã được lên lịch thành công từ $parsedStart đến $parsedEnd")
        } catch (e: DateTimeParseException) {
            ResponseEntity.badRequest().body("Lỗi: Định dạng ngày giờ không hợp lệ. Sử dụng 'yyyy-MM-ddTHH:mm'")
        } catch (e: Exception) {
            ResponseEntity.internalServerError().body("Lỗi khi lên lịch: ${e.message}")
        }
    }
}
