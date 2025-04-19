package com.example.androidApp.services

import com.example.androidApp.models.Auditorium
import com.example.androidApp.models.Showtime
import com.example.androidApp.repositories.AuditoriumRepository
import com.example.androidApp.repositories.MovieRepository
import com.example.androidApp.repositories.ShowtimeRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
@Service
class ShowtimeService(
    private val movieRepository: MovieRepository,
    private val auditoriumRepository: AuditoriumRepository,
    private val showtimeRepository: ShowtimeRepository
) {
    private val BREAK_TIME_MINUTES = 15

    fun scheduleShowtimes(startTime: LocalDateTime, endTime: LocalDateTime) {
        val movies = movieRepository.findAll()
        val auditoriums = auditoriumRepository.findAll()

        if (movies.isEmpty() || auditoriums.isEmpty()) return

        // Khởi tạo thời gian chiếu sớm nhất cho mỗi phòng
        val auditoriumTimes = auditoriums.associateWith { startTime }.toMutableMap()

        while (auditoriumTimes.any { (_, time) -> time.isBefore(endTime) }) {
            for (auditorium in auditoriums) {
                val nextAvailableTime = auditoriumTimes[auditorium] ?: continue
                if (nextAvailableTime.isAfter(endTime)) continue

                val movie = movies.randomOrNull() ?: continue
                val movieEndTime = nextAvailableTime.plusMinutes(movie.duration.toLong())

                // Kiểm tra phòng chiếu có trống không
                if (isAuditoriumAvailable(auditorium, nextAvailableTime, movie.duration)) {
                    val showtime = Showtime(
                        movie = movie,
                        auditorium = auditorium,
                        startTime = nextAvailableTime,
                        endTime = movieEndTime
                    )
                    showtimeRepository.save(showtime)
                    println("🎬 Scheduled '${movie.title}' in '${auditorium.name}' at $nextAvailableTime")

                    // Cập nhật thời gian suất chiếu tiếp theo
                    auditoriumTimes[auditorium] = movieEndTime.plusMinutes(BREAK_TIME_MINUTES.toLong())
                } else {
                    // Nếu không khả dụng, tìm thời gian mới
                    auditoriumTimes[auditorium] = nextAvailableTime.plusMinutes(BREAK_TIME_MINUTES.toLong())
                }
            }
        }

        val allShowtimes = showtimeRepository.findAll()
        println("📅 Lịch chiếu đã lưu vào DB:")
        allShowtimes.forEach {
            println("${it.movie.title} | ${it.auditorium.name} | ${it.startTime} - ${it.endTime}")
        }
    }


    private fun isAuditoriumAvailable(auditorium: Auditorium, startTime: LocalDateTime, movieDuration: Int): Boolean {
        val existingShowtimes = showtimeRepository.findByAuditoriumAndStartTimeBetween(
            auditorium,
            startTime.minusMinutes(1),
            startTime.plusMinutes(movieDuration + BREAK_TIME_MINUTES.toLong())
        )
        return existingShowtimes.isEmpty()
    }
    fun getShowtimesByMovieAndDate(movieId: Int, startTime: LocalDateTime, endTime: LocalDateTime): ResponseEntity<List<Showtime>> {
        val showtimes = showtimeRepository.findByMovieIdAndStartTimeBetween(movieId, startTime, endTime)
        return ResponseEntity.ok(showtimes)

    }


    fun getUpComingShowtimeDates(movieId: Int): List<LocalDate> {
        val today = LocalDate.now()
        val threeDaysLater = today.plusDays(4)

        return showtimeRepository.findUpcomingShowtimeDates(movieId, today, threeDaysLater)
            .map { it.toLocalDate() }
    }
    fun getShowtimeById(showtimeId: Int): Showtime?{
        return showtimeRepository.findById(showtimeId).orElse(null)
    }

}
