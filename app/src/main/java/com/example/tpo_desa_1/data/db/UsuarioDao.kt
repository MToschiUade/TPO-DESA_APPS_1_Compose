package com.example.tpo_desa_1.data.db

import androidx.room.*
import com.example.tpo_desa_1.data.model.Usuario

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE (alias = :identificador OR email = :identificador) AND password = :password")
    suspend fun login(identificador: String, password: String): Usuario?
}
