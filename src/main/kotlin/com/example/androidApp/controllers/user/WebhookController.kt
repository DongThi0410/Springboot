package com.example.androidApp.controllers.user

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class WebhookController {

    @PostMapping("/payment")
    fun receiveWebhook(@RequestBody data: String): ResponseEntity<String> {
        println("Webhook: " + data)
        return ResponseEntity.ok("received")
    }
}