package com.example.tpo_desa_1.data.model

data class IngredienteUI(
    var nombre: String = "",
    var cantidad: String = "1",
    var unidad: UnidadMedida = UnidadMedida.UNIDADES
)

enum class UnidadMedida(val displayName: String) {
    KILOS("Kilos"),
    GRAMOS("Gramos"),
    UNIDADES("Unidades"),
    LITROS("Litros"),
    MILILITROS("Mililitros")
}