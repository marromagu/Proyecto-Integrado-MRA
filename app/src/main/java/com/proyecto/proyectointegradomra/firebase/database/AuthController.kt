package com.proyecto.proyectointegradomra.firebase.database

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.proyecto.proyectointegradomra.data.TipoUsuario
import com.proyecto.proyectointegradomra.data.Usuario

class AuthController : ViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestoreController = FirestoreController()

    private val _userAuthcurrent = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    private val userAuthcurrent: LiveData<FirebaseUser?> = _userAuthcurrent

    private val _usuario = MutableLiveData<Usuario?>()
    val usuario: LiveData<Usuario?> = _usuario

    fun iniciarSesion(
        email: String, contrasena: String, onSuccess: () -> Unit, onError: (String) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, contrasena).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _userAuthcurrent.value = firebaseAuth.currentUser
                cargarUsuario()
                onSuccess()
            } else {
                // Maneja el error de inicio de sesión en la Ventana de Inicio de Sesión
                onError(task.exception?.message ?: "Error al iniciar sesión")
            }
        }
    }

    fun cargarUsuario() {
        val uid = obtenerUidUsuario() ?: return
        firestoreController.obtenerUsuarioPorUidFirestore(uid,
            onSuccess = { usuario ->
                usuario.uid = uid
                _usuario.value = usuario

                Log.d("AuthController_cargarUsuario", "Usuario cargado desde Firestore: $_usuario")
            },
            onFailure = { exception ->
                Log.e(
                    "AuthController",
                    "Error al cargar usuario desde Firestore: ${exception.message}"
                )
            }
        )
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
                    _userAuthcurrent.value = firebaseAuth.currentUser
                    val uid = obtenerUidUsuario()
                    if (uid != null) {
                        val tipoUsuario =
                            if (esOfertante) TipoUsuario.OFERTANTE else TipoUsuario.DEMANDANTE
                        val nuevoUsuario =
                            Usuario(uid = uid, nombre = nombre, email = correo, tipo = tipoUsuario)

                        firestoreController.agregarDocumentoUsuarioFirestore(nuevoUsuario)


                    } else {
                        // Maneja el error de crear usuario en la Ventana de Registrarse
                        onError("Error al crear el perfil de usuario.")
                    }
                    onSuccess()
                } else {
                    // Maneja el error de crear usuario en la Ventana de Registrarse
                    onError(task.exception?.message ?: "Error al registrarse")
                }
            }
    }

    fun cerrarSesion() {
        firebaseAuth.signOut()
        _userAuthcurrent.value = null
    }

    fun eliminarCuenta() {
        val uid = usuario.value?.uid ?: ""
        if (uid.isNotEmpty()) {
            userAuthcurrent.value?.delete()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firestoreController.eliminarDocumentoFirestore(
                        collectionPath = "usuarios",
                        documentId = uid,
                        onSuccess = {
                            Log.d(TAG, "Usuario eliminado de Firestore.")
                        },
                        onFailure = { exception ->
                            Log.e(TAG, "Error al eliminar documento: ${exception.message}")
                        }
                    )
                    Log.d(TAG, "AuthController:EliminarCuenta -> Usuario eliminado!!!.")
                } else {
                    Log.e(TAG, "Error al eliminar cuenta del usuario: ${task.exception?.message}")
                }
            }
        } else {
            Log.d(TAG, "AuthController:EliminarCuenta -> ERROR: Usuario no autenticado.")
        }
    }


    fun actualizarNombreUsuario(newName: String) {
        val uid = obtenerUidUsuario() ?: return
        val usuarioActual = _usuario.value

        if (usuarioActual != null) {
            usuarioActual.nombre = newName
            _usuario.value = usuarioActual

            firestoreController.actualizarNombreUsuarioFirestore(uid, newName, onSuccess = {
                println("Nombre actualizado correctamente en Firestore.")
            }, onFailure = { exception ->
                println("Error al actualizar el nombre en Firestore: ${exception.message}")
            })
        } else {
            println("No se encontró el usuario para actualizar el nombre.")
        }
    }

}