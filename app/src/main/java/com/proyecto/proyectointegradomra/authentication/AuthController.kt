package com.proyecto.proyectointegradomra.authentication

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.proyecto.proyectointegradomra.firestore.FirestoreController

class AuthController : ViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestoreController = FirestoreController()

    private val _user = MutableLiveData(firebaseAuth.currentUser)
    val user: MutableLiveData<FirebaseUser?> = _user

    fun obtenerDisplayNameUsuario(): String? {
        val user = Firebase.auth.currentUser
        return user?.displayName
    }

    fun actualizarNombreUsuario(nuevoNombre: String, onComplete: (Boolean) -> Unit) {
        val user = Firebase.auth.currentUser

        val profileUpdates = userProfileChangeRequest {
            displayName = nuevoNombre
        }

        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    // Método para iniciar sesión
    fun iniciarSesion(
        correo: String,
        contrasena: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(correo, contrasena).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _user.value = firebaseAuth.currentUser
                onSuccess()
            } else {
                onError(task.exception?.message ?: "Error al iniciar sesión")
            }
        }
    }

    // Método para registrarse
    fun registrarse(
        correo: String,
        contrasena: String,
        nombre: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _user.value = firebaseAuth.currentUser
                    firestoreController.agregarPerfilUsuario(
                        nombre = nombre,
                        email = correo
                    )
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
        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                }
            }
    }
}