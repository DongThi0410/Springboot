package com.example.androidApp.services

import com.example.androidApp.models.Movie
import com.example.androidApp.repositories.MovieRepository
import org.springframework.stereotype.Service

@Service
class MovieService(private val movieRepository: MovieRepository){
    fun getAllMovies(): List<Movie>{
        return movieRepository.findAll()
    }
    fun getMoviesByCate(id: Int): List<Movie>{
        return movieRepository.getMoviesByCateId(id).ifEmpty { emptyList() }
    }
    fun getMovieById(id: Int): Movie? {
        return movieRepository.findById(id).orElse(null)
    }
}