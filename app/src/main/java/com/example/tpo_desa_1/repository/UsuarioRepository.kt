package com.example.tpo_desa_1.repository

import com.example.tpo_desa_1.data.db.UsuarioDao
import com.example.tpo_desa_1.data.model.Usuario

class UsuarioRepository(private val dao: UsuarioDao) {
    suspend fun login(identificador: String, password: String): Usuario? {
        return dao.login(identificador, password)
    }

    suspend fun insertar(usuario: Usuario) {
        dao.insertar(usuario)
    }
}
