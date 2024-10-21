package com.proyecto.proyectointegradomra.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogInController : ViewModel() {
    fun updateNombre(it: String) {
        _nombre.value = it
    }

    fun updateContrasena(it: String) {
        _contrasena.value = it
    }

    private val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String> = _nombre

    private val _contrasena = MutableLiveData<String>()
    val contrasena: LiveData<String> = _contrasena

}