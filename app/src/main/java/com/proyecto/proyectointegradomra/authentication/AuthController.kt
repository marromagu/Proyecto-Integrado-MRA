package com.proyecto.proyectointegradomra.authentication

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.proyecto.proyectointegradomra.Data.TipoUsuario
import com.proyecto.proyectointegradomra.Data.Usuario
import com.proyecto.proyectointegradomra.firestore.FirestoreController

class AuthController : ViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestoreController = FirestoreController()

    private val _user = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    val user: LiveData<FirebaseUser?> = _user
    private val _usuario = MutableLiveData<Usuario?>()
    val usuario: LiveData<Usuario?> = _usuario

    fun iniciarSesion(
        email: String, contrasena: String, onSuccess: () -> Unit, onError: (String) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, contrasena).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _user.value = firebaseAuth.currentUser
                cargarUsuario()
                onSuccess()
            } else {
                onError(task.exception?.message ?: "Error al iniciar sesión")
            }
        }
    }

    fun cargarUsuario() {
        val uid = obtenerUidUsuario() ?: return
        firestoreController.obtenerUsuarioPorUid(uid, onSuccess = { usuario ->
            _usuario.value = usuario
        }, onFailure = { exception ->
            Log.e(TAG, "Error al cargar usuario: ${exception.message}")
        })
    }

    private fun obtenerUidUsuario(): String? {
        return firebaseAuth.currentUser?.uid
    }

    fun registrarse(
        correo: String,
        contrasena: String,
        nombre: String,
        esOfertante: Boolean,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _user.value = firebaseAuth.currentUser
                    val uid = obtenerUidUsuario()
                    if (uid != null) {
                        // Crear el objeto Usuario según el tipo
                        val tipoUsuario =
                            if (esOfertante) TipoUsuario.OFERTANTE else TipoUsuario.DEMANDANTE
                        val nuevoUsuario =
                            Usuario(uid = uid, nombre = nombre, correo = correo, tipo = tipoUsuario)

                        // Agregar el perfil del usuario a Firestore
                        firestoreController.agregarDocumentoUsuario(nuevoUsuario)


                    } else {
                        Log.e(
                            TAG,
                            "AuthController:Registrarse -> Error: UID de usuario es nulo después del registro"
                        )
                        onError("Error al crear el perfil de usuario.")
                    }
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "Error al registrarse")
                }
            }
    }

    fun cerrarSesion() {
        firebaseAuth.signOut()
        _user.value = null
    }

    fun eliminarCuenta() {
        val user = firebaseAuth.currentUser!!
        val uid = obtenerUidUsuario()
        if (uid != null) {
            user.delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firestoreController.eliminarDocumento(collectionPath = "usuarios",
                        documentId = uid ?: "",
                        onSuccess = {
                            println("Documento eliminado correctamente.")
                        },
                        onFailure = { exception ->
                            println("Error al eliminar documento: ${exception.message}")
                        })
                    Log.d(TAG, "AuthController:EliminarCuetna -> Usuario eliminado!!!.")
                }
            }
        } else {
            Log.d(TAG, "AuthController:EliminarCuetna -> ERROR: Usuario no autenticado.")
        }

    }

    fun actualizarNombreUsuario(newName: String) {
        val uid = obtenerUidUsuario() ?: return // Obtener el UID del usuario actual
        val usuarioActual = _usuario.value // Obtener el objeto Usuario actual

        // Verificar si el objeto Usuario existe
        if (usuarioActual != null) {
            // Actualizar el nombre en el objeto Usuario
            usuarioActual.nombre = newName
            _usuario.value = usuarioActual // Notificar a los observadores del cambio

            // Actualizar el nombre en Firestore
            firestoreController.actualizarNombreUsuario(uid, newName,
                onSuccess = {
                    println("Nombre actualizado correctamente en Firestore.")
                },
                onFailure = { exception ->
                    println("Error al actualizar el nombre en Firestore: ${exception.message}")
                }
            )
        } else {
            println("No se encontró el usuario para actualizar el nombre.")
        }
    }

}