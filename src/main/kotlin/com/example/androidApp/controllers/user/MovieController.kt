package com.example.androidApp.controllers.user

import com.example.androidApp.models.Movie
import com.example.androidApp.services.MovieService
import com.example.androidApp.services.TicketService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("public")
@CrossOrigin(origins = ["http://localhost:3000", "http://localhost:4200"])

class MovieController(
    private val movieService: MovieService,
    private val ticketService: TicketService
){
    @GetMapping("/movies")
    fun getMovie(): ResponseEntity<List<Movie>>{
        return ResponseEntity.ok(movieService.getAllMovies())
    }

    @GetMapping("/movie/{id}")
    fun getMovieById(@PathVariable id: Int): ResponseEntity<Movie>{
        println("Received id: $id")  // Thêm log để kiểm tra
        val movie = movieService.getMovieById(id)
        return if (movie != null) {
            ResponseEntity.ok(movie)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/movies/withCate/{id}")
    fun getMovieByCate(@PathVariable id: Int): ResponseEntity<List<Movie>>{
        val movie = movieService.getMoviesByCate(id)
        return if (movie.isNotEmpty()) {
            ResponseEntity.ok(movie)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/movies/search")
    fun search(@RequestParam query: String): ResponseEntity<Any>{
        val movies = movieService.search(query)
        return if (movies.isNotEmpty()){
            ResponseEntity.ok(movies)
        }else
            ResponseEntity.badRequest().body("Không có kết quả trùng khớp nội dung tìm kiếm ")
    }


}