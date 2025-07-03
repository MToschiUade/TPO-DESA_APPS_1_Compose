package com.example.tpo_desa_1.data.model.response

import com.google.gson.annotations.SerializedName

data class UsuarioDetalleDTO(
    @SerializedName("name")
    val name: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("ubicacion")
    val ubicacion: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("status")
    val status: Boolean,

    @SerializedName("email")
    val email: String

)
