package com.proyecto.proyectointegradomra.Data

data class Usuario(
    val uid: String = "",
    var nombre: String = "",
    val correo: String = "",
    val tipo: TipoUsuario = TipoUsuario.OTRO  // "Ofertante" o "Demandante"
)
enum class TipoUsuario {
    OFERTANTE,
    DEMANDANTE,
    OTRO
}