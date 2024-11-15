package com.proyecto.proyectointegradomra.data.model

data class Usuario(
    var uid: String = "",
    var name: String = "",
    var email: String = "",
    var type: TipoUsuarios = TipoUsuarios.CONSUMIDOR,  // "Ofertante" o "Consumidor"
)