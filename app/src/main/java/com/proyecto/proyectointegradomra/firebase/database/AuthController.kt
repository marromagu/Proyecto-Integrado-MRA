package com.proyecto.proyectointegradomra.firebase.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.proyecto.proyectointegradomra.data.TipoUsuario
import com.proyecto.proyectointegradomra.data.Usuario
import kotlinx.coroutines.launch

class AuthController : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestoreController = FirestoreController()

    private val _userAuthcurrent = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    private val userAuthcurrent: LiveData<FirebaseUser?> = _userAuthcurrent

    private val _usuario = MutableLiveData<Usuario?>()
    val usuario: LiveData<Usuario?> = _usuario

    fun iniciarSesion(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
        _userAuthcurrent.value = firebaseAuth.currentUser
        cargarUsuario()
    }

    fun registrarse(email: String, password: String, name: String, esOfertante: Boolean) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
        _userAuthcurrent.value = firebaseAuth.currentUser
        val uid = obtenerUidUsuario()
        val tipoUsuario = if (esOfertante) TipoUsuario.OFERTANTE else TipoUsuario.DEMANDANTE
        val nuevoUsuario = Usuario(uid = uid ?: "", nombre = name, email = email, tipo = tipoUsuario)
        firestoreController.agregarDocumentoUsuarioFirestore(nuevoUsuario)
    }

    fun cerrarSesion() {
        firebaseAuth.signOut()
        _userAuthcurrent.value = null
    }

    fun eliminarCuenta() {
        val uid = usuario.value?.uid ?: ""
        userAuthcurrent.value?.delete()
        firestoreController.eliminarDocumentoFirestore(collectionPath = "usuarios",documentId = uid)
    }

    fun actualizarNombreUsuario(newName: String) {
        val uid = obtenerUidUsuario() ?: return
        val usuarioActual = _usuario.value
        usuarioActual?.nombre = newName
        _usuario.value = usuarioActual
        firestoreController.actualizarNombreUsuarioFirestore(uid, newName)
    }

    fun cargarUsuario() {
        viewModelScope.launch {
            val uid = obtenerUidUsuario() ?: return@launch
            val usuario = firestoreController.obtenerUsuarioPorUidFirestore(uid)
            usuario?.uid = uid
            _usuario.value = usuario
        }
    }

    private fun obtenerUidUsuario(): String? {
        return firebaseAuth.currentUser?.uid
    }
}