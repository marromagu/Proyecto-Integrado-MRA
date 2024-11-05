package com.proyecto.proyectointegradomra.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogInViewModel : ViewModel() {
    fun updateName(it: String) {
        _name.value = it
    }

    fun updatePassword(it: String) {
        _password.value = it
    }

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

}