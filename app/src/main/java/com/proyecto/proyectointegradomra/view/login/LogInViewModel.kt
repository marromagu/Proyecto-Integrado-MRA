package com.proyecto.proyectointegradomra.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogInViewModel : ViewModel() {
    fun updateNombre(it: String) {
        _nombre.value = it
    }

    fun updateContrasenna(it: String) {
        _contrasenna.value = it
    }

    private val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String> = _nombre

    private val _contrasenna = MutableLiveData<String>()
    val contrasenna: LiveData<String> = _contrasenna

}