package com.proyecto.proyectointegradomra.ui.updateAd

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyecto.proyectointegradomra.data.model.Publicacion
import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UpdateAdViewModel(publicacion: Publicacion) : ViewModel() {

    private val _publicacion = MutableLiveData(publicacion)
    val publicacion: MutableLiveData<Publicacion> = _publicacion

    private val _title = MutableLiveData(publicacion.title)
    val title: MutableLiveData<String> = _title

    private val _description = MutableLiveData(publicacion.description)
    val description: MutableLiveData<String> = _description

    private val _plazas = MutableLiveData(publicacion.size)
    val plazas: MutableLiveData<Int> = _plazas

    private val dateLong: Long = publicacion.date
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm a", Locale.getDefault())

    private val dateString = dateFormat.format(Date(dateLong))
    private val timeString = timeFormat.format(Date(dateLong))

    private val _fecha = MutableLiveData(dateString)
    val fecha: MutableLiveData<String> = _fecha

    private val _hora = MutableLiveData(timeString)
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
