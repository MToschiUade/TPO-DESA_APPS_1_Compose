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

    @Query("DELETE FROM comentarios WHERE recetaId = :recetaId AND autor = :autor")
    suspend fun eliminarSiEsDelAutor(recetaId: Int, autor: String): Int

    @Insert
    suspend fun insertarTodos(comentarios: List<Comentario>)

    @Query("DELETE FROM comentarios")
    suspend fun borrarTodos()

}
