package com.example.androidApp.services

import com.example.androidApp.models.VerificationToken
import com.example.androidApp.repositories.VerificationTokenRepo
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class VerificationTokenService(
    private val verificationTokenRepo: VerificationTokenRepo,
    private val userService: UserService
) {
    fun findByToken(token: String): VerificationToken? {
        return verificationTokenRepo.findByToken(token)
    }

    fun findAllByExpiryDateBefore(): List<VerificationToken> {
        return verificationTokenRepo.findAllByExpiryDateBefore(LocalDateTime.now())
    }

    fun save(verificationToken: VerificationToken): VerificationToken {
        return this.verificationTokenRepo.save(verificationToken)
    }

    fun delete(verificationToken: VerificationToken) {
        return this.verificationTokenRepo.delete(verificationToken)
    }

    @Transactional
    fun cleanupToken() {
        val expiredTokens = findAllByExpiryDateBefore()

        expiredTokens.forEach { token ->
            val user = token.user
            if (!user.enabled) {
                delete(token)
                userService.delete(user)
            }
        }
    }


    @Scheduled(cron = "0 0 * * * *")
    fun scheduleCleanup() {
        cleanupToken()
    }

}