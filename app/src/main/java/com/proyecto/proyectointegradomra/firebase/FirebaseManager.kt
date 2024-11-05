package com.proyecto.proyectointegradomra.firebase

import com.proyecto.proyectointegradomra.data.Usuario
import com.proyecto.proyectointegradomra.firebase.database.AuthController


class FirebaseManager {
    private val miAuthController = AuthController()

    fun getUsuarioActual(): Usuario? {
        return miAuthController.usuario.value
    }

}