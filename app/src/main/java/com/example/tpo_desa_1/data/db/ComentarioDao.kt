package com.example.tpo_desa_1.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tpo_desa_1.data.model.Comentario

@Dao
interface ComentarioDao {

    @Query("SELECT * FROM comentarios WHERE recetaId = :recetaId ORDER BY fecha DESC")
    suspend fun obtenerPorReceta(recetaId: Int): List<Comentario>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(comentarios: List<Comentario>)

    @Query("DELETE FROM comentarios WHERE id = :comentarioId AND autor = :autor")
    suspend fun eliminarSiEsDelAutor(comentarioId: Int, autor: String): Int
}
