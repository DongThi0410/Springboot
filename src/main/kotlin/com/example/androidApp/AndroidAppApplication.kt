package com.example.androidApp

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import java.io.FileInputStream
@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class AndroidAppApplication

fun main(args: Array<String>) {
	runApplication<AndroidAppApplication>(*args)
}
