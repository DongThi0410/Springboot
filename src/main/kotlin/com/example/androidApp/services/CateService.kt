package com.example.androidApp.services

import com.example.androidApp.models.Category
import com.example.androidApp.repositories.CateRepository
import org.springframework.stereotype.Service

@Service
class CateService(private val cateRepository: CateRepository){
    fun getAllCate(): List<Category>{
        return cateRepository.findAll()
    }

}