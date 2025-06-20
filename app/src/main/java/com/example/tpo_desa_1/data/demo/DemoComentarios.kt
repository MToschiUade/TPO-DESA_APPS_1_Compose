package com.example.tpo_desa_1.data.demo

import com.example.tpo_desa_1.data.model.Comentario

val demoComentarios = listOf(
    Comentario(
        recetaId = 2,
        autor = "Mati",
        contenido = "¡Muy rica!",
        estado = "aprobado",
        fecha = System.currentTimeMillis(),
        fechaRevision = System.currentTimeMillis(),
        puntaje = 3
    ),
    Comentario(
        recetaId = 2,
        autor = "Sofi",
        contenido = "Fácil de hacer y deliciosa.",
        estado = "aprobado",
        fecha = System.currentTimeMillis() - 1000000,
        fechaRevision = System.currentTimeMillis() - 800000,
        puntaje = 1
    )
)
