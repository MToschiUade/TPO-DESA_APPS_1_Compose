package com.example.tpo_desa_1.data.model

data class PasoPreparacionUI(
    var descripcion: String = "",
    var mediaUri: String? = null, // Imagen o video opcional
    var tipoMedia: TipoMedia? = null
)

enum class TipoMedia {
    IMAGEN, VIDEO
}
