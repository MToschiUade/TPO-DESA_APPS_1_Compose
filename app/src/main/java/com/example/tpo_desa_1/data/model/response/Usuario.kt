package com.example.tpo_desa_1.data.model.response

data class Usuario(
    val id: Int,
    val description: String?,
    val username: String,
    val email: String,
    val name: String,
    val lastName: String,
    val password: String,
    val urlImage: String?,
    val ubicacion: String?,
    val role: String,
    val status: Boolean
)