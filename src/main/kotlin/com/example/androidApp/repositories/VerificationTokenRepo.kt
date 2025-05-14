package com.example.androidApp.repositories

import com.example.androidApp.models.VerificationToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface VerificationTokenRepo : JpaRepository<VerificationToken, Int> {

    fun findByToken(token: String): VerificationToken?
    fun findAllByExpiryDateBefore(now: LocalDateTime): List<VerificationToken>
}