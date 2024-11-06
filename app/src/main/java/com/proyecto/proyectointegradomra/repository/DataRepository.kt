package com.proyecto.proyectointegradomra.repository

import com.proyecto.proyectointegradomra.firebase.services.AuthService
import com.proyecto.proyectointegradomra.firebase.services.FirestoreService

class DataRepository(
    private val authController: AuthService,
    private val firestoreController: FirestoreService
) {

    val usuario = authController.usuario

    // Función para cerrar sesión
    fun cerrarSesion() = authController.cerrarSesion()

    // Función para eliminar cuenta
    fun eliminarCuenta() = authController.eliminarCuenta()

    // Función para iniciar sesión
    fun iniciarSesion(email: String, password: String, onSuccess: () -> Unit, onError: () -> Unit) {

        authController.iniciarSesion(email, password, onSuccess, onError)
    }

    // Función para registrarse
    fun registrarse(
        email: String,
        password: String,
        name: String,
        esOfertante: Boolean,
        onSuccess: () -> Unit, onError: () -> Unit
    ) {
        authController.registrarse(email, password, name, esOfertante, onSuccess, onError)
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
