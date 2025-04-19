package com.example.androidApp.repositories

import com.example.androidApp.models.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CateRepository: JpaRepository<Category, Int> {
}