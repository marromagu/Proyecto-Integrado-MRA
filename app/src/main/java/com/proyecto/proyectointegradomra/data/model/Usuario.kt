package com.proyecto.proyectointegradomra.data.model

data class Usuario(
    var uid: String = "",
    var name: String = "",
    var email: String = "",
    val type: TipoUsuarios = TipoUsuarios.CONSUMIDOR,  // "Ofertante" o "Demandante"
)