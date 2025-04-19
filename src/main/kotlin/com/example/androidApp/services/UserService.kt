package com.example.androidApp.services

import com.example.androidApp.models.User
import com.example.androidApp.repositories.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val userRepository: UserRepository) {

    fun save(user: User): User {
        return this.userRepository.save(user)
    }
    fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }
    fun existsByPhone(phone: String): Boolean {
        return userRepository.existsByPhone(phone)
    }
    fun findByEmail(email: String): User? {
        return this.userRepository.findByEmail(email)
    }

    fun getById(id: Int): User? {
        return userRepository.findById(id).orElse(null)
    }


}