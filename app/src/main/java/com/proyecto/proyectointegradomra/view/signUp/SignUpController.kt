package com.proyecto.proyectointegradomra.view.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpController : ViewModel() {

    fun updateNombre(it: String) {
        _nombre.value = it
    }

    fun updateCorreo(it: String) {
        _correo.value = it
    }

    fun updateContrasena(it: String) {
        _contrasena.value = it
    }

    fun updateRepetirContrasena(it: String) {
        _repetirContrasena.value = it
    }
    fun updateEsOfertante(it: Boolean) {
        _esOfertante.value = it
    }

    private val _esOfertante = MutableLiveData<Boolean>()
    val esOfertante: LiveData<Boolean> = _esOfertante

    private val _repetirContrasena = MutableLiveData<String>()
    val repetirContrasena: LiveData<String> = _repetirContrasena

    private val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String> = _nombre

    private val _contrasena = MutableLiveData<String>()
    val contrasena: LiveData<String> = _contrasena

    private val _correo = MutableLiveData<String>()
    val correo: LiveData<String> = _correo

}