package com.proyecto.proyectointegradomra.Data

data class Productor(
    val usuario: Usuario = Usuario(),
    val actividades: List<String> = emptyList()  // Lista de IDs de actividades publicadas
)