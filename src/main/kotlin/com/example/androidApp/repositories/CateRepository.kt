package com.example.androidApp.repositories

import com.example.androidApp.models.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository

interface CateRepository: JpaRepository<Category, Int> {
}