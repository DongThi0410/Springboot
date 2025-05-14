package com.example.androidApp.controllers.admin

import com.example.androidApp.dtos.MovieDTO
import com.example.androidApp.dtos.MovieUpdateDTO
import com.example.androidApp.services.MovieService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("admin")
@RestController
class MovieAdminController(private val movieService: MovieService) {
    @PostMapping("/movie/delete/{id}")
    fun deleteMovie(@PathVariable("id") id: Int): ResponseEntity<String> {
        return if (movieService.delete(id)) {
            ResponseEntity.ok("Xóa phim thành công")
        } else
            ResponseEntity.badRequest().body("Xóa phim toán thất bại")
    }

    @PutMapping("/movie/update/{id}")
    fun update(@PathVariable("id") id: Int, @RequestBody request: MovieUpdateDTO): ResponseEntity<String> {
        return if (movieService.update(id, request)) {
            ResponseEntity.ok("Câp nhật thành công")
        } else {
            ResponseEntity.badRequest().body("Câp nhật thành công")

        }
    }

    @PostMapping("/movie/add")
    fun add(@RequestBody movieDTO: MovieDTO): ResponseEntity<String> {
        return if(movieService.createMovie(movieDTO)){
            ResponseEntity.ok("Thêm phim mới thành công")
        }else
            ResponseEntity.badRequest().body("Thêm phim mới thất bại")
    }

}