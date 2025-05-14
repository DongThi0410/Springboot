package com.example.androidApp.services

import com.example.androidApp.controllers.ResetPasswordRequest
import com.example.androidApp.models.User
import com.example.androidApp.repositories.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val mailSender: EmailService,
    private val passwordEncoder: PasswordEncoder
) {

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
    fun findByPhone(phone: String): User? {
        return this.userRepository.findByPhone(phone)
    }

    fun getById(id: Int): User? {
        return userRepository.findById(id).orElse(null)
    }

    fun delete(user: User) {
        return this.userRepository.delete(user)
    }

    fun sendOtp(email: String){
        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("Email no exist")

        val otp = (100000..999999).random().toString()
        user.otp = otp
        user.otpExpiresAt = LocalDateTime.now().plusMinutes(5)
        userRepository.save(user)
        mailSender.sendOtpToReset(
            to = email,
            subject = "Mã OTP đặt lại mật khẩu",
            body = "Mã OTP của bạn là: $otp (hết hạn sau 5 phút)"
        )
    }

    fun resetPassword(request: ResetPasswordRequest){
        val user = userRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("Email khong hop le")

        if (user.otp != request.otp || user.otpExpiresAt!!.isBefore(LocalDateTime.now())){
            throw IllegalArgumentException("OTP khong hop le hoac da het han")
        }
        user.password = request.newPassword
        user.otp = null
        user.otpExpiresAt = null
        userRepository.save(user)
    }
}