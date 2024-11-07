package com.proyecto.proyectointegradomra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.proyecto.proyectointegradomra.navigation.NavigationManager
import com.proyecto.proyectointegradomra.ui.theme.ProyectoIntegradoMRATheme
import com.proyecto.proyectointegradomra.repository.DataRepository
import com.proyecto.proyectointegradomra.firebase.services.AuthService
import com.proyecto.proyectointegradomra.firebase.services.FirestoreService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Inicializa el controlador de navegación localmente en el contexto composable
            val navController = rememberNavController()

            // Instancia de DataRepository
            val authService = AuthService()
            val dataRepository = DataRepository(authService)

            ProyectoIntegradoMRATheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavigationManager(navController = navController, dataRepository = dataRepository)
                }
            }
        }
    }
}
