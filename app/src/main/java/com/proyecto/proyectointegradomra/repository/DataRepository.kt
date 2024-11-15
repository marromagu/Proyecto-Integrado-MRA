package com.proyecto.proyectointegradomra.repository

import androidx.lifecycle.ViewModel
import com.proyecto.proyectointegradomra.data.model.Publicaciones
import com.proyecto.proyectointegradomra.data.model.TipoPublicaciones
import com.proyecto.proyectointegradomra.firebase.services.AuthService
import com.proyecto.proyectointegradomra.firebase.services.FirestoreService


class DataRepository(
    private val autService: AuthService,
    private val firestoreService: FirestoreService
) : ViewModel() {

    // Función para cerrar sesión
    fun cerrarSesion() = autService.cerrarSesion()

    // Función para eliminar cuenta
    fun eliminarCuenta() = autService.eliminarCuenta()

    // Función para iniciar sesión
    fun iniciarSesion(email: String, password: String, onSuccess: () -> Unit, onError: () -> Unit) {
        autService.iniciarSesion(email, password, onSuccess, onError)
    }

    // Función para registrarse
    fun registrarse(
        email: String,
        password: String,
        name: String,
        esOfertante: Boolean,
        onSuccess: () -> Unit, onError: (Exception) -> Unit
    ) {
        autService.registrarse(email, password, name, esOfertante, onSuccess, onError = onError)
    }

    // Función para obtener el usuario actual
    fun obtenerUsuarioActual() = autService.usuario

    // Función para cargar el usuario desde Firestore para obtener sus datos
    fun cargarUsuario() = autService.cargarUsuario()

    // Función para actualizar el nombre de usuario
    fun actualizarNombreUsuario(uid: String, newName: String) {
        firestoreService.actualizarNombreUsuarioFirestore(uid, newName)
    }

    /*-------------------------------------------------------------------------------------------*/

    // Función para agregar un documento a Firestore
    fun agregarDocumentoPublicacionesFirestore(miPublicacion: Publicaciones) {
        firestoreService.agregarDocumentoPublicacionesFirestore(miPublicacion)
    }

    suspend fun obtenerPublicacionesPorUsuario(userId: String): List<Publicaciones> {
        return firestoreService.obtenerPublicacionesPorUsuario(userId)
    }

    suspend fun obtenerPublicaciones(tipo: TipoPublicaciones): List<Publicaciones> {
        return firestoreService.obtenerPublicaciones(tipo)
    }

    fun addParticipantes(uid: String, publicacionId: String) {
        firestoreService.addParticipantes(uid, publicacionId)
    }
}