package com.example.androidApp.controllers.user

import com.example.androidApp.models.Movie
import com.example.androidApp.services.MovieService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("public")
@CrossOrigin(origins = ["http://localhost:3000", "http://localhost:4200"])

class MovieController( private val movieService: MovieService){
    @GetMapping("/movies")
    fun getMovie(): ResponseEntity<List<Movie>>{
        return ResponseEntity.ok(movieService.getAllMovies())
    }

    @GetMapping("/movie/{id}")
    fun getMovieById(@PathVariable id: Int): ResponseEntity<Movie>{
        println("Received id: $id")  // Thêm log để kiểm tra
        if (id == null) {
            return ResponseEntity.badRequest().body(null)
        }
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


}