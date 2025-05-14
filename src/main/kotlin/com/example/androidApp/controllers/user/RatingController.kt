package com.example.androidApp.controllers.user

import com.example.androidApp.services.MovieService
import com.example.androidApp.services.RatingDTO
import com.example.androidApp.services.RatingService
import com.example.androidApp.services.TicketService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("user")

class RatingController(
    private val movieService: MovieService,
    private val ratingService: RatingService

){
    @PostMapping("/rating")
    fun rating(@RequestBody dto: RatingDTO): ResponseEntity<String> {
        return try {
            ratingService.rating(dto)
            ResponseEntity.ok("Đánh giá thành công.")
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body("Lỗi: ${e.message}")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi máy chủ: ${e.message}")
        }
    }

    @GetMapping("/rate/{id}")
    fun getRates(@PathVariable id: Int): ResponseEntity<Any>{
        return try {
            val result = ratingService.getRating(id)
            ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body("Lỗi: ${e.message}")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi máy chủ: ${e.message}")
        }
    }


}