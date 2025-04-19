package com.example.androidApp.controllers.user

import com.example.androidApp.models.Category
import com.example.androidApp.services.CateService
import com.example.androidApp.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("public/cate")
@CrossOrigin(origins = ["http://localhost:3000", "http://localhost:4200"])

class CateController(
    private val userService: UserService,
    private val cateService: CateService
) {
    @GetMapping
    fun getCate(): ResponseEntity<List<Category>> {
        return ResponseEntity.ok(cateService.getAllCate())
    }
}