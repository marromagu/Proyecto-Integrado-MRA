package com.proyecto.proyectointegradomra.Data

data class Demandante(
    val usuario: Usuario = Usuario(),
    val anuncios: List<String> = emptyList()  // Lista de IDs de anuncios creados
)