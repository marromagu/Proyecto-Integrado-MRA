package com.proyecto.proyectointegradomra.data

data class Usuario(
    var uid: String = "",
    var nombre: String = "",
    var email: String = "",
    val tipo: TipoUsuario = TipoUsuario.OTRO  // "Ofertante" o "Demandante"
)
enum class TipoUsuario {
    OFERTANTE,
    DEMANDANTE,
    OTRO
}