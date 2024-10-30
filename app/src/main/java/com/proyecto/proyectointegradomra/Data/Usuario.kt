package com.proyecto.proyectointegradomra.Data

data class Usuario(
    val nombre: String = "",
    val correo: String = "",
    val tipo: String = ""  // "productor" o "consumidor"
)