package com.proyecto.proyectointegradomra.Data

data class Consumidor(
    val usuario: Usuario = Usuario(),
    val anuncios: List<String> = emptyList()  // Lista de IDs de anuncios creados
)