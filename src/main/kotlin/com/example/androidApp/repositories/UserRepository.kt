package com.example.androidApp.repositories

import com.example.androidApp.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository

interface UserRepository: JpaRepository<User, Int> {
    fun findByEmail(email:String): User?
    fun existsByEmail(email: String): Boolean
    fun existsByPhone(phone: String): Boolean
}
