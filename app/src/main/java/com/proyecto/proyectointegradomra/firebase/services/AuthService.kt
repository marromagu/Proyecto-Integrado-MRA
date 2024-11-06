package com.proyecto.proyectointegradomra.firebase.services

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.proyecto.proyectointegradomra.data.model.TipoUsuario
import com.proyecto.proyectointegradomra.data.model.Usuario
import kotlinx.coroutines.launch

/**
 * Clase AuthController: ViewModel responsable de manejar la autenticación y la sesión del usuario.
 * Extiende ViewModel para vincularse al ciclo de vida de la actividad o fragmento,
 * garantizando que se mantenga una única instancia de AuthController mientras el propietario exista.
 * Esto permite un manejo eficiente y persistente de datos de autenticación, evitando recreaciones innecesarias. *
 */
class AuthService : ViewModel() {

    // Instancia singleton de autenticación de Firebase
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // Controlador Firestore para gestionar la base de datos
    private val firestoreController = FirestoreService()

    // LiveData para el usuario autenticado actualmente
    private val _userAuthCurrent = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    val userAuthCurrent: LiveData<FirebaseUser?> = _userAuthCurrent

    // LiveData para los datos del usuario (información adicional a FirebaseUser)
    private val _usuario = MutableLiveData<Usuario?>()
    val usuario: LiveData<Usuario?> = _usuario

    /**
     * Función para iniciar sesión en Firebase usando email y contraseña.
     * Actualiza el estado de autenticación y carga los datos del usuario.
     */
    fun iniciarSesion(email: String, password: String, onSuccess: () -> Unit, onError: () -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _userAuthCurrent.value = firebaseAuth.currentUser
                    cargarUsuario() // Cargar datos del usuario desde Firestore
                    onSuccess()
                } else {
                    // Manejo de errores de autenticación
                    _userAuthCurrent.value = null
                    onError()
                }
            }
    }

    /**
     * Función para registrar un nuevo usuario en Firebase y guardarlo en Firestore.
     * Crea una entrada de usuario en la base de datos Firestore.
     */
    fun registrarse(
        email: String,
        password: String,
        name: String,
        esOfertante: Boolean,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _userAuthCurrent.value = firebaseAuth.currentUser
                    val uid = obtenerUidUsuario() ?: return@addOnCompleteListener
                    val tipoUsuario =
                        if (esOfertante) TipoUsuario.OFERTANTE else TipoUsuario.DEMANDANTE
                    val nuevoUsuario =
                        Usuario(uid = uid, name = name, email = email, type = tipoUsuario)
                    firestoreController.agregarDocumentoUsuarioFirestore(nuevoUsuario)
                    onSuccess()
                } else {
                    // Manejo de errores en el registro
                    _userAuthCurrent.value = null
                    onError()
                }
            }
    }

    /**
     * Función para cerrar sesión en Firebase.
     * Resetea el estado de autenticación y borra los datos locales del usuario.
     */
    fun cerrarSesion() {
        firebaseAuth.signOut()
        _userAuthCurrent.value = null
        _usuario.value = null
    }

    /**
     * Función para eliminar la cuenta del usuario autenticado.
     * Borra el usuario de Firebase Authentication y elimina su documento en Firestore.
     */
    fun eliminarCuenta() {
        val uid = usuario.value?.uid ?: return
        userAuthCurrent.value?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firestoreController.eliminarDocumentoFirestore("usuarios", uid)
                _userAuthCurrent.value = null
                _usuario.value = null
            } else {
                Log.e("AuthService", "Error al eliminar la cuenta: ${task.exception}")
            }
        }
    }

    /**
     * Función para actualizar el nombre del usuario en Firestore.
     * Actualiza el nombre en la base de datos y refleja el cambio en la LiveData.
     */
    fun actualizarNombreUsuario(newName: String) {
        val uid = obtenerUidUsuario() ?: return
        val usuarioActual = _usuario.value ?: return

        usuarioActual.name = newName // Actualizar el valor en la instancia actual
        _usuario.value = usuarioActual // Reflejar el cambio en la LiveData

        firestoreController.actualizarNombreUsuarioFirestore(uid, newName)
    }

    /**
     * Función para cargar los datos del usuario autenticado desde Firestore.
     * Esta función usa coroutines para realizar la carga de forma asíncrona.
     */
    fun cargarUsuario() {
        viewModelScope.launch {
            val uid = obtenerUidUsuario() ?: return@launch
            val usuarioFirestore = firestoreController.obtenerUsuarioPorUidFirestore(uid)
            usuarioFirestore?.uid = uid
            _usuario.value = usuarioFirestore // Actualiza el usuario en la LiveData
        }
    }

    /**
     * Función privada para obtener el UID del usuario autenticado.
     * Retorna null si no hay usuario autenticado.
     */
    private fun obtenerUidUsuario(): String? {
        return firebaseAuth.currentUser?.uid
    }
}
