package com.example.tpo_desa_1.data.db

import androidx.room.*
import com.example.tpo_desa_1.data.model.Receta

@Dao
interface RecetaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(receta: Receta)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(recetas: List<Receta>)

    @Query("DELETE FROM recetas")
    suspend fun borrarTodas()

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

    @Query("SELECT * FROM recetas WHERE autor = :autor")
    suspend fun obtenerRecetasPorUsuario(autor: String): List<Receta>

    @Query("SELECT * FROM recetas WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Receta?

    @Query("SELECT * FROM recetas WHERE id IN (:ids)")
    suspend fun obtenerPorIds(ids: List<Int>): List<Receta>

    // Guardado de recetas futuras (comentado por ahora)
    // @Query("SELECT * FROM recetas WHERE emailUsuario = :email")
    // suspend fun obtenerRecetasGuardadas(email: String): List<Receta>
}
