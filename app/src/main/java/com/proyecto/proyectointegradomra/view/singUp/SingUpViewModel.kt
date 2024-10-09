package com.proyecto.proyectointegradomra.view.singUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SingUpViewModel : ViewModel() {
    fun updateNombre(it: String) {
        _nombre.value = it
    }

    fun updateCorreo(it: String) {
        _correo.value = it
    }

    fun updateContrasenna(it: String) {
        _contrasenna.value = it
    }

    private val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String> = _nombre

    private val _contrasenna = MutableLiveData<String>()
    val contrasenna: LiveData<String> = _contrasenna

    private val _correo = MutableLiveData<String>()
    val correo: LiveData<String> = _correo


}