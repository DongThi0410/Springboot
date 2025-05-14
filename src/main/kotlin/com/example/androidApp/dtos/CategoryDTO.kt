package com.example.androidApp.dtos

import com.example.androidApp.models.Category

data class CategoryDTO(
    val id: Int,
    val name: String
)

fun CategoryDTO.isEntity(): Category{
    val cate = Category()
    cate.id = this.id
    cate.name = this.name
    return cate
}