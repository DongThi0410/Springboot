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
import java.util.ArrayDeque

@Service
class ShowtimeService(
    private val movieRepository: MovieRepository,
    private val auditoriumRepository: AuditoriumRepository,
    private val showtimeRepository: ShowtimeRepository
) {
    private val BREAK_TIME_MINUTES = 15

    fun scheduleShowtimes(fromDate: LocalDate, toDate: LocalDate, dailyStart: LocalTime, dailyEnd: LocalTime) {
        val movies = movieRepository.findAll()
        val auditoriums = auditoriumRepository.findAll()

        if (movies.isEmpty() || auditoriums.isEmpty()) return

        val movieQueue = ArrayDeque(movies)
        for (date in fromDate.datesUntil(toDate.plusDays(1))) {

            val dayStart = LocalDateTime.of(date, dailyStart)
            val dayEnd = LocalDateTime.of(date, dailyEnd)
            val auditoriumTimes = auditoriums.associateWith { dayStart }.toMutableMap()
            while (auditoriumTimes.any { (_, time) -> time.isBefore(dayEnd) }) {
                for (auditorium in auditoriums) {
                    val nextAvailableTime = auditoriumTimes[auditorium] ?: continue
                    if (nextAvailableTime.isAfter(dayEnd)) continue

                    val movie = movieQueue.removeFirst() ?: continue
                    val movieEndTime = nextAvailableTime.plusMinutes(movie.duration.toLong())

                    if (isAuditoriumAvailable(auditorium, nextAvailableTime, movie.duration)) {
                        val showtime = Showtime(
                            movie = movie,
                            auditorium = auditorium,
                            startTime = nextAvailableTime,
                            endTime = movieEndTime
                        )
                        showtimeRepository.save(showtime)
                        println("🎬 Scheduled '${movie.title}' in '${auditorium.name}' at $nextAvailableTime")

                        auditoriumTimes[auditorium] = movieEndTime.plusMinutes(BREAK_TIME_MINUTES.toLong())
                    } else {
                        auditoriumTimes[auditorium] = nextAvailableTime.plusMinutes(BREAK_TIME_MINUTES.toLong())
                    }
                    movieQueue.addLast(movie)
                }
            }
        }

        val allShowtimes = showtimeRepository.findAll()
        println("📅 Lịch chiếu đã lưu vào DB:")

    }


    private fun isAuditoriumAvailable(auditorium: Auditorium, startTime: LocalDateTime, movieDuration: Int): Boolean {
        val endTime = startTime.plusMinutes((movieDuration + BREAK_TIME_MINUTES).toLong())
        val showtimes = showtimeRepository.findByAuditorium(auditorium)
        return showtimes.none() { exist ->
            val existingStart = exist.startTime
            val existingEnd = exist.endTime
            (startTime< existingEnd) && (endTime > existingStart)

        }
    }

    fun getShowtimesByMovieAndDate(
        movieId: Int,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): ResponseEntity<List<Showtime>> {
        val showtimes = showtimeRepository.findByMovieIdAndStartTimeBetween(movieId, startTime, endTime)
        return ResponseEntity.ok(showtimes)

    }


    fun getUpComingShowtimeDates(movieId: Int): List<LocalDate> {
        val today = LocalDate.now()
        val threeDaysLater = today.plusDays(4)

        return showtimeRepository.findUpcomingShowtimeDates(movieId, today, threeDaysLater)
            .map { it.toLocalDate() }
    }

    fun getShowtimeById(showtimeId: Int): Showtime? {
        return showtimeRepository.findById(showtimeId).orElse(null)
    }

}
