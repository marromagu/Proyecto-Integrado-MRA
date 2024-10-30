package com.proyecto.proyectointegradomra.authentication

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.proyecto.proyectointegradomra.firestore.FirestoreController

class AuthController : ViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestoreController = FirestoreController()

    private val _user = MutableLiveData(firebaseAuth.currentUser)
    val user: MutableLiveData<FirebaseUser?> = _user
    private val _nombreUsuario = MutableLiveData<String?>(null)
    val nombreUsuario: MutableLiveData<String?> = _nombreUsuario

    enum class TipoUsuario {
        OFERTANTE, DEMANDANTE
    }


    fun obtenerNombreUsuario() {
        val uid = obtenerUidUsuario()
        firestoreController.obtenerNombreUsuarioPorUid(uid = uid ?: "", onSuccess = { nombre ->
            _nombreUsuario.value = nombre
            if (nombre != null) {
                println("Nombre del usuario: $nombre")
            } else {
                println("Usuario no encontrado")
            }
        }, onFailure = { exception ->
            println("Error al obtener el nombre del usuario: ${exception.message}")
        })
    }

    fun obtenerUidUsuario(): String? {
        return _user.value?.uid
    }

    private fun agregarPerfilUsuario(
        ui: String?, nombre: String, email: String, tipo: TipoUsuario
    ) {
        firestoreController.agregarDocumnetoUsuario(ui, nombre, email, tipo)
    }

    fun iniciarSesion(
        email: String, contrasena: String, onSuccess: () -> Unit, onError: (String) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, contrasena).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _user.value = firebaseAuth.currentUser
                onSuccess()
            } else {
                onError(task.exception?.message ?: "Error al iniciar sesión")
            }
        }
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
                    if (esOfertante) {
                        agregarPerfilUsuario(uid, nombre, correo, TipoUsuario.OFERTANTE)
                    } else {
                        agregarPerfilUsuario(uid, nombre, correo, TipoUsuario.DEMANDANTE)
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
                Log.d(TAG, "User account deleted.")
            }
        }
    }
}