package com.example.androidApp.repositories

import com.example.androidApp.models.Auditorium
import org.springframework.data.jpa.repository.JpaRepository

interface AuditoriumRepository: JpaRepository<Auditorium, Int> {

}
