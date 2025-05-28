package com.example.tpo_desa_1.data.db

import androidx.room.*
import com.example.tpo_desa_1.data.model.Receta

@Dao
interface RecetaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(receta: Receta)

    @Query("SELECT * FROM recetas WHERE nombre = :nombre")
    suspend fun obtenerPorNombre(nombre: String): Receta?

    @Query("SELECT * FROM recetas")
    suspend fun obtenerTodas(): List<Receta>

    @Query("SELECT * FROM recetas WHERE estado = 'aprobada' ORDER BY fechaRevision DESC LIMIT 3")
    suspend fun obtenerRecientesAprobadas(): List<Receta>

    @Query("SELECT * FROM recetas WHERE estado = 'aprobada'")
    suspend fun obtenerAprobadas(): List<Receta>

    @Delete
    suspend fun eliminar(receta: Receta)
}
