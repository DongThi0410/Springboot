package com.example.androidApp.services

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
@Service
class EmailService(private val mailSender: JavaMailSender) {

    fun sendVerificationEmail(to: String, token: String) {
        val message = SimpleMailMessage()
        message.setTo(to)
        message.setSubject("Xác minh tài khoản của bạn")
        message.setText("Nhấn vào link để xác minh: https://f7d5-2405-4802-605a-add0-c5cd-385c-cd38-6672.ngrok-free.app/auth/verify?token=$token")
        mailSender.send(message)
    }
    fun sendOtpToReset(to: String, subject: String, body: String){
        val msg = SimpleMailMessage()
        msg.setTo(to)
        msg.setSubject(subject)
        msg.setText(body)
        mailSender.send(msg)
    }
}
