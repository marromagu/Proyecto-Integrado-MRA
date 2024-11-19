package com.proyecto.proyectointegradomra.firebase.services

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.proyecto.proyectointegradomra.data.model.TipoUsuarios
import com.proyecto.proyectointegradomra.data.model.Usuario
import kotlinx.coroutines.launch

/**
 * Clase AuthService: ViewModel para manejar la autenticación y sesión de usuarios.
 * Utiliza Firebase Authentication y Firestore para las operaciones de autenticación y almacenamiento.
 * Extiende ViewModel para gestionar eficientemente el ciclo de vida y los datos asociados.
 */
class AuthService : ViewModel() {

    // Instancia de FirebaseAuth para manejar la autenticación
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // Servicio para interactuar con Firestore
    private val firestoreController = FirestoreService()

    // LiveData para observar al usuario autenticado actualmente
    private val _userAuthCurrent = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    private val userAuthCurrent: LiveData<FirebaseUser?> = _userAuthCurrent

    // LiveData para observar los datos del usuario autenticado desde Firestore
    private val _usuario = MutableLiveData<Usuario?>()
    val usuario: LiveData<Usuario?> = _usuario

    /**
     * Inicia sesión con un correo electrónico y contraseña.
     * Si tiene éxito, actualiza el usuario autenticado y carga los datos del usuario desde Firestore.
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @param onSuccess Callback ejecutado si el inicio de sesión es exitoso.
     * @param onError Callback ejecutado si ocurre un error en el inicio de sesión.
     */
    fun iniciarSesion(email: String, password: String, onSuccess: () -> Unit, onError: () -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _userAuthCurrent.value = firebaseAuth.currentUser
                    cargarUsuario() // Carga los datos del usuario desde Firestore
                    onSuccess()
                } else {
                    // Manejo de errores de autenticación
                    Log.e("AuthService", "Error al iniciar sesión: ${task.exception}")
                    _userAuthCurrent.value = null
                    onError()
                }
            }
    }

    /**
     * Registra un nuevo usuario en Firebase Authentication y lo guarda en Firestore.
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
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _userAuthCurrent.value = firebaseAuth.currentUser
                    val uid = obtenerUidUsuario() ?: return@addOnCompleteListener
                    val tipoUsuarios =
                        if (esOfertante) TipoUsuarios.OFERTANTE else TipoUsuarios.CONSUMIDOR
                    val nuevoUsuario = Usuario(
                        uid = uid,
                        name = name,
                        email = email,
                        type = tipoUsuarios
                    )
                    firestoreController.agregarDocumentoUsuarioFirestore(nuevoUsuario)
                    onSuccess()
                } else {
                    // Manejo de errores en el registro
                    Log.e("AuthService", "Error al registrar usuario: ${task.exception}")
                    _userAuthCurrent.value = null
                    onError(task.exception ?: Exception("Error desconocido"))
                }
            }
    }

    /**
     * Cierra la sesión del usuario actualmente autenticado.
     * Resetea el estado de autenticación y borra los datos locales del usuario.
     */
    fun cerrarSesion() {
        firebaseAuth.signOut()
        _userAuthCurrent.value = null
        _usuario.value = null
    }

    /**
     * Elimina la cuenta del usuario autenticado, tanto en Firebase Authentication como en Firestore.
     * Maneja errores potenciales durante el proceso.
     */
    fun eliminarCuenta() {
        val uid = usuario.value?.uid ?: return
        userAuthCurrent.value?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                viewModelScope.launch {
                    firestoreController.eliminarUsuario(uid)
                    _userAuthCurrent.value = null
                    _usuario.value = null
                }
            } else {
                Log.e("AuthService", "Error al eliminar la cuenta: ${task.exception}")
            }
        }
    }

    /**
     * Carga los datos del usuario autenticado desde Firestore.
     * Usa una coroutine para realizar esta operación de forma asíncrona.
     */
    fun cargarUsuario() {
        viewModelScope.launch {
            val uid = obtenerUidUsuario() ?: return@launch
            val usuarioFirestore = firestoreController.obtenerUsuarioPorUidFirestore(uid)
            if (usuarioFirestore != null) {
                usuarioFirestore.uid = uid
                _usuario.value = usuarioFirestore // Actualiza los datos del usuario
            } else {
                Log.e("AuthService", "No se encontró el usuario con UID: $uid")
            }
        }
    }

    /**
     * Obtiene el UID del usuario autenticado actualmente.
     * Retorna null si no hay usuario autenticado.
     * @return UID del usuario autenticado o null.
     */
    private fun obtenerUidUsuario(): String? {
        return firebaseAuth.currentUser?.uid
    }
}