package com.example.tpo_desa_1.data.model.request

import com.google.gson.annotations.SerializedName

data class ChangePasswordDTO(
    @SerializedName("email")
    val email: String,

    @SerializedName("totpCode")
    val totpCode: String,

    @SerializedName("newpass")
    val newpass: String
)
