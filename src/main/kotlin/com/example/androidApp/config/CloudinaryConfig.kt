package com.example.androidApp.config

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CloudinaryConfig {
    @Bean
    fun cloudinary(): Cloudinary {
        val config = mapOf(
            "cloud_name" to "dbz5cwmws",
            "api_key" to "339899675619624",
            "api_secret" to "yFV6zg00Mj3vAMJDXQB5W1eJrEI"
        )
        return Cloudinary(config)
    }
}