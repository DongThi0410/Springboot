package com.example.androidApp.controllers.user

import com.example.androidApp.dtos.UserDTO
import com.example.androidApp.models.User
import com.example.androidApp.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("user")
class UserController(
    private val userService: UserService
){
    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Int): ResponseEntity<User?>{
        if (id == null){
            return ResponseEntity.badRequest().body(null)
        }
        val user = userService.getById(id)
        return if (user!=null){
            ResponseEntity.ok(user)
        }else{
            ResponseEntity.notFound().build()
        }
    }
}