package com.proyecto.proyectointegradomra.repository

import androidx.lifecycle.ViewModel
import com.proyecto.proyectointegradomra.data.model.Publicaciones
import com.proyecto.proyectointegradomra.firebase.services.AuthService
import com.proyecto.proyectointegradomra.firebase.services.FirestoreService


class DataRepository(
    private val autService: AuthService,
    private val firestoreService: FirestoreService
) : ViewModel() {

    val usuario = autService.usuario

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
        onSuccess: () -> Unit, onError: () -> Unit
    ) {
        autService.registrarse(email, password, name, esOfertante, onSuccess, onError)
    }

    // Función para obtener el usuario actual
    fun obtenerUsuarioActual() = autService.usuario

    // Función para cargar el usuario desde Firestore
    fun cargarUsuario() = autService.cargarUsuario()

    // Función para actualizar el nombre de usuario
    fun actualizarNombreUsuario(newName: String) = autService.actualizarNombreUsuario(newName)

    /*-------------------------------------------------------------------------------------------*/

    // Función para agregar una publicación a Firestore
    fun agregarPublicacionAUsuario(uid: String, miPublicacionList: List<Publicaciones>) {
        firestoreService.agregarPublicacionAUsuario(uid, miPublicacionList)
    }

    // Función para agregar un documento a Firestore
    fun agregarDocumentoPublicacionesFirestore(miPublicacion: Publicaciones) {
        firestoreService.agregarDocumentoPublicacionesFirestore(miPublicacion)
    }

    // Función para cargar publicaciones por UID de usuario
    suspend fun cargarPublicacionesPorUidUsuario(uidUsuario: String): List<Publicaciones> {
        return firestoreService.cargarPublicacionesPorUidUsuario(uidUsuario)
    }

}