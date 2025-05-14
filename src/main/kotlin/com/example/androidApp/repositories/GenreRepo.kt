package com.example.androidApp.repositories

import com.example.androidApp.models.Genre
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GenreRepo : JpaRepository<Genre, Int>{
}