package com.proyecto.proyectointegradomra.repository

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.proyectointegradomra.data.model.Publicacion
import com.proyecto.proyectointegradomra.data.model.TipoPublicaciones
import com.proyecto.proyectointegradomra.firebase.services.AuthService
import com.proyecto.proyectointegradomra.firebase.services.FirestoreService
import kotlinx.coroutines.launch

/**
 * Repositorio centralizado que actúa como capa intermedia entre los servicios de Firebase y la lógica del ViewModel.
 * Proporciona métodos para gestionar autenticación y operaciones de Firestore relacionadas con publicaciones y usuarios.
 * Extiende ViewModel para integrarse con el ciclo de vida de Android.
 *
 * @property authService Servicio de autenticación (AuthService).
 * @property firestoreService Servicio de Firestore (FirestoreService).
 */
class DataRepository(
    private val authService: AuthService,
    private val firestoreService: FirestoreService
) : ViewModel() {

    /* -------------------------------------------------------------------------------------------
     * MÉTODOS RELACIONADOS CON LA AUTENTICACIÓN
     * ---------------------------------------------------------------------------------------- */

    /**
     * Cierra la sesión del usuario actual.
     */
    fun cerrarSesion() = authService.cerrarSesion()

    /**
     * Elimina la cuenta del usuario actual, tanto en Firebase Authentication como en Firestore.
     */
    fun eliminarCuenta() = authService.eliminarCuenta()

    /**
     * Inicia sesión con un correo electrónico y contraseña.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @param onSuccess Callback ejecutado si el inicio de sesión es exitoso.
     * @param onError Callback ejecutado si ocurre un error en el inicio de sesión.
     */
    fun iniciarSesion(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        authService.iniciarSesion(email, password, onSuccess, onError)
    }

    /**
     * Registra un nuevo usuario en Firebase Authentication y lo guarda en Firestore.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @param name Nombre del usuario.
     * @param esOfertante Indica si el usuario es ofertante o consumidor.
     * @param onSuccess Callback ejecutado si el registro es exitoso.
     * @param onError Callback ejecutado si ocurre un error en el registro.
     */
    fun registrarse(
        email: String,
        password: String,
        name: String,
        esOfertante: Boolean,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        authService.registrarse(email, password, name, esOfertante, onSuccess, onError)
    }

    /**
     * Obtiene un objeto LiveData con el usuario autenticado actual.
     * @return LiveData<Usuario?> con los datos del usuario actual.
     */
    fun obtenerUsuarioActual() = authService.usuario

    /**
     * Carga los datos del usuario autenticado desde Firestore.
     * Útil para refrescar la información del usuario en la aplicación.
     */
    fun cargarUsuario() = authService.cargarUsuario()

    /**
     * Actualiza el nombre del usuario en Firestore.
     *
     * @param uid UID del usuario.
     * @param newName Nuevo nombre para el usuario.
     */
    fun actualizarNombreUsuario(uid: String, newName: String) {
        firestoreService.actualizarNombreUsuarioFirestore(uid, newName)
    }

    /* -------------------------------------------------------------------------------------------
     * MÉTODOS RELACIONADOS CON PUBLICACIONES
     * ---------------------------------------------------------------------------------------- */

    /**
     * Agrega una nueva publicación a Firestore.
     *
     * @param miPublicacion Objeto Publicaciones a agregar.
     */
    fun agregarDocumentoPublicacionesFirestore(miPublicacion: Publicacion) {
        firestoreService.agregarDocumentoPublicacionesFirestore(miPublicacion)
    }

    /**
     * Obtiene las publicaciones creadas por un usuario específico.
     *
     * @param userId ID del usuario.
     * @return Lista de publicaciones asociadas al usuario.
     */
    suspend fun obtenerPublicacionesPorUsuario(userId: String): List<Publicacion> {
        return firestoreService.obtenerPublicacionesPorUsuario(userId)
    }

    /**
     * Obtiene publicaciones de un tipo específico en las que el usuario no participa.
     *
     * @param tipo Tipo de publicaciones.
     * @param uid UID del usuario que consulta.
     * @return Lista de publicaciones que cumplen con el criterio.
     */
    suspend fun obtenerPublicacionesPorTipoSinParticipar(
        tipo: TipoPublicaciones,
        uid: String
    ): List<Publicacion> {
        return firestoreService.obtenerPublicacionesPorTipoSinParticipar(tipo, uid)
    }

    /**
     * Agrega un participante a una publicación específica validando que haya plazas disponibles.
     *
     * @param uid UID del participante a agregar.
     * @param miPublicacion Objeto Publicacion al que se agregará el participante.
     */
    fun agregarParticipante(
        uid: String,
        miPublicacion: Publicacion,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            val listaDeParticipantes =
                firestoreService.obtenerListaDeParticipantes(miPublicacion.uid)
            val plazas = firestoreService.obtenerPlazas(miPublicacion.uid)
            if (plazas <= listaDeParticipantes.size) {
                Log.i("DataRepository", "No hay plazas disponibles")
                onError()
                return@launch
            } else {
                firestoreService.agregarParticipante(
                    uid,
                    miPublicacion.uid
                )
                onSuccess()
            }
        }

    }

    /**
     * Obtiene publicaciones de un tipo específico en las que el usuario ya participa.
     *
     * @param uid UID del usuario que consulta.
     * @return Lista de publicaciones que cumplen con el criterio.
     */
    suspend fun obtenerPublicacionesParticipadas(uid: String): List<Publicacion> {
        return firestoreService.obtenerPublicacionesParticipadas(uid)
    }

    /**
     * Elimina un participante de una publicación específica.
     *
     * @param uid UID del participante a eliminar.
     * @param publicacionId ID de la publicación.
     */
    fun eliminarParticipante(uid: String, publicacionId: String) {
        firestoreService.eliminarParticipante(uid, publicacionId)
    }

    /**
     * Elimina una publicación específica.
     *
     * @param publicacionId ID de la publicación a eliminar.
     */
    fun eliminarPublicacion(publicacionId: String) {
        firestoreService.eliminarPublicacion(publicacionId)
    }

    /**
     * Actualiza los datos de una publicación en Firestore.
     *
     * @param publicacion Objeto Publicacion con los nuevos datos.
     */
    fun actualizarPublicacion(publicacion: Publicacion) {
        firestoreService.actualizarPublicacion(publicacion)
    }

}
