package com.proyecto.proyectointegradomra.ui.createAd

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CreateAdViewModel : ViewModel() {
    private val _title = MutableLiveData<String>()
    val title: MutableLiveData<String> = _title

    private val _description = MutableLiveData<String>()
    val description: MutableLiveData<String> = _description

    private val _plazas = MutableLiveData<Int>()
    val plazas: MutableLiveData<Int> = _plazas

    private val _fecha = MutableLiveData<String>()
    val fecha: MutableLiveData<String> = _fecha

    private val _hora = MutableLiveData<String>()
    val hora: MutableLiveData<String> = _hora

    fun updateTitle(it: String) {
        _title.value = it
    }

    fun updateDescription(it: String) {
        _description.value = it
    }

    fun updatePlazas(it: Int) {
        _plazas.value = it
    }

    fun updateFecha(it: String) {
        _fecha.value = it
    }

    fun updateHora(it: String) {
        _hora.value = it
    }
}