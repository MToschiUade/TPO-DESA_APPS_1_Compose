package com.example.tpo_desa_1.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tpo_desa_1.data.model.PasoReceta

@Dao
interface PasoRecetaDao {

    @Query("SELECT * FROM pasos_receta WHERE recetaId = :recetaId ORDER BY orden ASC")
    suspend fun obtenerPorReceta(recetaId: Int): List<PasoReceta>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(pasos: List<PasoReceta>)
}
