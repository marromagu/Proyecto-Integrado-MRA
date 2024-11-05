package com.proyecto.proyectointegradomra.ui.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {

    fun updateName(it: String) {
        _name.value = it
    }

    fun updateEmail(it: String) {
        _email.value = it
    }

    fun updatePassword(it: String) {
        _password.value = it
    }

    fun updateRepeatPassword(it: String) {
        _repeatPassword.value = it
    }

    fun updateEsOfertante(it: Boolean) {
        _esOfertante.value = it
    }

    private val _esOfertante = MutableLiveData<Boolean>()
    val esOfertante: LiveData<Boolean> = _esOfertante

    private val _repeatPassword = MutableLiveData<String>()
    val repeatPassword: LiveData<String> = _repeatPassword

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

}