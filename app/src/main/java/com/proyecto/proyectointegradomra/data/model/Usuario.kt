package com.proyecto.proyectointegradomra.data.model

data class Usuario(
    var uid: String = "",
    var nombre: String = "",
    var email: String = "",
    val tipo: TipoUsuario = TipoUsuario.OTRO  // "Ofertante" o "Demandante"
)