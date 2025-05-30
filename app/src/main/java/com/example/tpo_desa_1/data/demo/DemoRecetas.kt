package com.example.tpo_desa_1.data.demo

import com.example.tpo_desa_1.data.model.Receta
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.collections.listOf

    // Para conversión de fechas a LONG TODO: revisar cuando se intgre con el back
    // Para la demo se utilizó https://timestamp.online/

val demoRecetas = listOf(
    Receta(
        nombre = "Tacos demo",
        puntaje = 5,
        tiempo = 30,
        estado = "aprobada",
        fechaRevision = 1748439630,
        imagenPortadaUrl = "https://www.themealdb.com/images/media/meals/uvuyxu1503067369.jpg",
        autor = "MelinaD"
    ), // fecha de revisión 28/5/2025, 10:40:30

    Receta(
        nombre = "Pasta demo",
        puntaje = 4,
        tiempo = 20,
        estado = "rechazada",
        fechaRevision = 1747748430,
        imagenPortadaUrl = "https://www.themealdb.com/images/media/meals/wvqpwt1468339226.jpg",
        autor = "MelinaD"
    ), // fecha de revisión 20/5/2025, 10:40:30

    Receta(
        nombre = "Pizza demo",
        puntaje = 3,
        tiempo = 25,
        estado = "rechazada",
        fechaRevision = 1748008530,
        imagenPortadaUrl = "https://www.themealdb.com/images/media/meals/x0lk931587671540.jpg",
        autor = "MelinaD"
    ), // fecha de revisión 23/5/2025, 10:55:30

    Receta(
        nombre = "Hamburguesa demo",
        puntaje = 5,
        tiempo = 15,
        estado = "pendiente",
        fechaRevision = null,
        imagenPortadaUrl = "https://www.carniceriademadrid.es/wp-content/uploads/2022/09/smash-burger-que-es.jpg",
        autor = "MelinaD"
    ),

    Receta(
        nombre = "Empanadas demo",
        puntaje = 4,
        tiempo = 40,
        estado = "aprobada",
        fechaRevision = 1747834830,
        imagenPortadaUrl = "https://peopleenespanol.com/thmb/px5GarUe0rxAr66lKEaboMetCHM=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/GettyImages-1158987157-2000-25ff8f49b6af4027ac4a62eed28effd7.jpg",
        autor = "MelinaD"
    ), // fecha de revisión 21/5/2025, 10:40:30

    Receta(
        nombre = "Ceviche demo",
        puntaje = 4,
        tiempo = 50,
        estado = "pendiente",
        fechaRevision = null,
        imagenPortadaUrl = "https://es.cravingsjournal.com/wp-content/uploads/2018/08/ceviche-con-leche-de-tigre-2.jpg",
        autor = "MelinaD"
    ),

    Receta(
        nombre = "Ramen demo",
        puntaje = 3,
        tiempo = 60,
        estado = "aprobada",
        fechaRevision = 1747835130,
        imagenPortadaUrl = "https://resizer.glanacion.com/resizer/v2/a-shoyu-ramen-in-gray-bowl-on-concrete-table-top-3CCBARMPDRFI5O6H5OPS3PP66E.jpg?auth=5f4d38a8750849d33b7c6e6cd4aec32bcd639658006140e82c66d27189a3ca47&width=1280&height=854&quality=70&smart=true",
        autor = "MelinaD"
    ), // fecha de revisión 21/5/2025, 10:45:30

    Receta(
        nombre = "Sushi demo",
        puntaje = 5,
        tiempo = 45,
        estado = "aprobada",
        fechaRevision = 1747921530,
        imagenPortadaUrl = "https://sushisun.com.ar/wp-content/uploads/2020/08/WhatsApp-Image-2024-12-12-at-8.38.51-PM.jpeg",
        autor = "IvanaC"
    ), // fecha de revisión 22/5/2025, 10:45:30

    Receta(
        nombre = "Paella demo",
        puntaje = 5,
        tiempo = 90,
        estado = "aprobada",
        fechaRevision = 1748007930,
        imagenPortadaUrl = "https://www.recetasnestle.com.ar/sites/default/files/srh_recipes/876038bcd1cf5abcaa28e86d9705eaf6.jpg",
        autor = "IvanaC"
    ), // fecha de revisión 23/5/2025, 10:45:30

    Receta(
        nombre = "Milanesa demo",
        puntaje = 4,
        tiempo = 35,
        estado = "rechazada",
        fechaRevision = 1748008230,
        imagenPortadaUrl = "https://breaders.com.ar/web/wp-content/uploads/2023/07/banner-ofertas-800x1000-1-800x675.jpg",
        autor = "IvanaC"
    ) // fecha de revisión 23/5/2025, 10:50:30
)
