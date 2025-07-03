package com.example.tpo_desa_1.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tpo_desa_1.data.model.Ingrediente

@Dao
interface IngredienteDao {

    @Query("SELECT * FROM ingredientes WHERE recetaId = :recetaId")
    suspend fun obtenerPorReceta(recetaId: Int): List<Ingrediente>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(ingredientes: List<Ingrediente>)

    @Query("DELETE FROM ingredientes")
    suspend fun borrarTodos()
}
