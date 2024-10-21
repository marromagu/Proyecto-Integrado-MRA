package com.proyecto.proyectointegradomra.Authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthController : ViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _user = MutableLiveData(firebaseAuth.currentUser)
    val user: MutableLiveData<FirebaseUser?> = _user

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

    fun registrarse(
        correo: String,
        contrasena: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _user.value = firebaseAuth.currentUser
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
}