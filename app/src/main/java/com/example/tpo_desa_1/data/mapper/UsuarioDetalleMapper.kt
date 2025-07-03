package com.example.tpo_desa_1.data.mapper

import com.example.tpo_desa_1.data.model.UsuarioDetalle
import com.example.tpo_desa_1.data.model.response.UsuarioDetalleDTO

fun UsuarioDetalleDTO.toModel(): UsuarioDetalle {
    return UsuarioDetalle(
        nombre = name,
        apellido = lastName,
        ubicacion = ubicacion,
        descripcion = description,
        status = status,
        email = email // nuevo campo
    )
}
