package com.proyecto.proyectointegradomra.repository

import com.proyecto.proyectointegradomra.firebase.services.AuthService
import com.proyecto.proyectointegradomra.firebase.services.FirestoreService

class DataRepository(
    private val authController: AuthService,
    private val firestoreController: FirestoreService
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

    // Función para cargar el usuario desde Firestore
    fun cargarUsuario() {
        authController.cargarUsuario()
    }

    // Función para actualizar el nombre de usuario
    fun actualizarNombreUsuario(newName: String) {
        authController.actualizarNombreUsuario(newName)
    }
}
