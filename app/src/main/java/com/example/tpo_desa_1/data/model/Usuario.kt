package com.example.tpo_desa_1.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey val email: String,
    val alias: String,
    val password: String = ""
)

