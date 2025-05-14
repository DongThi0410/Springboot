package com.example.androidApp.services

import com.example.androidApp.models.Rating
import com.example.androidApp.repositories.MovieRepository
import com.example.androidApp.repositories.RateRepository
import com.example.androidApp.repositories.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class RatingService(
    private val rateRepo: RateRepository,
    private val movieRepository: MovieRepository,
    private val userRepository: UserRepository
) {
    fun rating(rate: RatingDTO) {
        val movie = movieRepository.findById(rate.movieId)
            .orElseThrow { IllegalArgumentException("Không tìm thấy phim") }

        val user = userRepository.findById(rate.userId)
            .orElseThrow { IllegalArgumentException("khong tim thay tai khoan nguoi dung") }
        rateRepo.save(
            Rating(
                user = user,
                movie = movie,
                score = rate.score,
                comment = rate.comment?.takeIf { it.isNotBlank() }),
        )
    }

    fun getRating(id: Int): List<RatingResponseDTO> {
        val rating = rateRepo.findByMovieId(id).ifEmpty { emptyList() }
        return rating.map {
            RatingResponseDTO(
                userId = it.user.id,
                username = it.user.name,
                score = it.score,
                comment = it.comment,
                ratedAt = it.createdAt.toString()
            )
        }
    }
}

data class RatingDTO(
    val userId: Int,
    val movieId: Int,
    val score: Int,
    val comment: String?
)

data class RatingResponseDTO(
    val userId: Int,
    val username: String,
    val score: Int,
    val comment: String?,
    val ratedAt: String
)

