package com.example.tpo_desa_1.data.db

import androidx.room.*
import com.example.tpo_desa_1.data.model.RecetaGuardada

@Dao
interface RecetaGuardadaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(recetaGuardada: RecetaGuardada)

    @Delete
    suspend fun eliminar(recetaGuardada: RecetaGuardada)

    @Query("SELECT * FROM recetas_guardadas WHERE usuarioEmail = :email ORDER BY fechaGuardado DESC")
    suspend fun obtenerPorUsuarioOrdenado(email: String): List<RecetaGuardada>

    @Query("SELECT EXISTS(SELECT 1 FROM recetas_guardadas WHERE usuarioEmail = :email AND recetaId = :recetaId)")
    suspend fun estaGuardada(email: String, recetaId: Int): Boolean
}
