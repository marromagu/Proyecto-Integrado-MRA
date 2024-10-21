package com.proyecto.proyectointegradomra.view.singUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SingUpController : ViewModel() {

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

    private val _repetirContrasena = MutableLiveData<String>()
    val repetirContrasena: LiveData<String> = _repetirContrasena

    private val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String> = _nombre

    private val _contrasena = MutableLiveData<String>()
    val contrasena: LiveData<String> = _contrasena

    private val _correo = MutableLiveData<String>()
    val correo: LiveData<String> = _correo

}