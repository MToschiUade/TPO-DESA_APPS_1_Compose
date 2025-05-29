package com.example.tpo_desa_1.data.demo

import com.example.tpo_desa_1.data.model.Receta
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.collections.listOf

val demoRecetas = listOf(

    // Para conversión de fechas a LONG TODO: revisar cuando se intgre con el back
    // Para la demo se utilizó https://timestamp.online/

    Receta("Tacos demo", 5, 30, "aprobada", 1748439630, "https://www.themealdb.com//images//media//meals//uvuyxu1503067369.jpg", "MelinaD"), // fecha de revisión 28/5/2025, 10:40:30
    Receta("Pasta demo", 4, 20, "rechazada",1747748430, "https://www.themealdb.com//images//media//meals//wvqpwt1468339226.jpg", "MelinaD"), // fecha de revisión 20/5/2025, 10:40:30
    Receta("Pizza demo", 3, 25, "rechazada",1748008530, "https://www.themealdb.com//images//media//meals//x0lk931587671540.jpg", "MelinaD"), // fecha de revisión 23/5/2025, 10:55:30
    Receta("Hamburguesa demo", 5, 15, "pendiente",null,"https://www.carniceriademadrid.es/wp-content/uploads/2022/09/smash-burger-que-es.jpg", "MelinaD"),
    Receta("Empanadas demo", 4, 40, "aprobada",1747834830,"https://peopleenespanol.com/thmb/px5GarUe0rxAr66lKEaboMetCHM=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/GettyImages-1158987157-2000-25ff8f49b6af4027ac4a62eed28effd7.jpg", "MelinaD"), // fecha de revisión 21/5/2025, 10:40:30
    Receta("Ceviche demo", 4, 50, "pendiente", null,"https://es.cravingsjournal.com/wp-content/uploads/2018/08/ceviche-con-leche-de-tigre-2.jpg", "MelinaD"),
    Receta("Ramen demo", 3, 60, "aprobada",1747835130,"https://resizer.glanacion.com/resizer/v2/a-shoyu-ramen-in-gray-bowl-on-concrete-table-top-3CCBARMPDRFI5O6H5OPS3PP66E.jpg?auth=5f4d38a8750849d33b7c6e6cd4aec32bcd639658006140e82c66d27189a3ca47&width=1280&height=854&quality=70&smart=true", "MelinaD"), // fecha de revisión 21/5/2025, 10:45:30
    Receta("Sushi demo", 5, 45, "aprobada",1747921530,"https://sushisun.com.ar/wp-content/uploads/2020/08/WhatsApp-Image-2024-12-12-at-8.38.51-PM.jpeg", "IvanaC"), // fecha de revisión 22/5/2025, 10:45:30
    Receta("Paella demo", 5, 90, "aprobada",1748007930,"https://www.recetasnestle.com.ar/sites/default/files/srh_recipes/876038bcd1cf5abcaa28e86d9705eaf6.jpg", "IvanaC"), // fecha de revisión 23/5/2025, 10:45:30
    Receta("Milanesa demo", 4, 35, "rechazada",1748008230,"https://breaders.com.ar/web/wp-content/uploads/2023/07/banner-ofertas-800x1000-1-800x675.jpg", "IvanaC") // fecha de revisión 23/5/2025, 10:50:30
)
