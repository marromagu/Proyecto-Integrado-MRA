package com.proyecto.proyectointegradomra.data

data class Demandante(
    val usuario: Usuario = Usuario(),
    val anuncios: List<String> = emptyList()  // Lista de IDs de anuncios creados
)