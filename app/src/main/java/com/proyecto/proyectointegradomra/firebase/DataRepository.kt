package com.proyecto.proyectointegradomra.firebase

import com.proyecto.proyectointegradomra.firebase.database.AuthController
import com.proyecto.proyectointegradomra.firebase.database.FirestoreController

class DataRepository(
    private val authController: AuthController,
    private val firestoreController: FirestoreController
) {
    // Función para iniciar sesión
    fun iniciarSesion(email: String, password: String) =
        authController.iniciarSesion(email, password)

    // Función para registrarse
    fun registrarse(email: String, password: String, name: String, esOfertante: Boolean) {
        authController.registrarse(email, password, name, esOfertante)
    }

    // Función para obtener el usuario actual
    fun obtenerUsuarioActual() = authController.usuario

    fun actualizarNombreUsuario(newName: String) {
        authController.actualizarNombreUsuario(newName)
    }
}
