package com.example.tpo_desa_1.data.demo

import com.example.tpo_desa_1.data.model.Receta
import java.time.LocalDateTime
import java.time.ZoneOffset

val demoRecetas = listOf(

    // Para conversión de fechas a LONG TODO: revisar cuando se intgre con el back
    // Para la demo se utilizó https://timestamp.online/

    Receta("Tacos demo", 5, 30, "aprobada", 1748439630), // fecha de revisión 28/5/2025, 10:40:30
    Receta("Pasta demo", 4, 20, "rechazada",1747748430), // fecha de revisión 20/5/2025, 10:40:30
    Receta("Pizza demo", 3, 25, "rechazada",1748008530), // fecha de revisión 23/5/2025, 10:55:30
    Receta("Hamburguesa demo", 5, 15, "pendiente",null),
    Receta("Empanadas demo", 4, 40, "aprobada",1747834830), // fecha de revisión 21/5/2025, 10:40:30
    Receta("Ceviche demo", 4, 50, "pendiente", null),
    Receta("Ramen demo", 3, 60, "aprobada",1747835130), // fecha de revisión 21/5/2025, 10:45:30
    Receta("Sushi demo", 5, 45, "aprobada",1747921530), // fecha de revisión 22/5/2025, 10:45:30
    Receta("Paella demo", 5, 90, "aprobada",1748007930), // fecha de revisión 23/5/2025, 10:45:30
    Receta("Milanesa demo", 4, 35, "rechazada",1748008230) // fecha de revisión 23/5/2025, 10:50:30
)
