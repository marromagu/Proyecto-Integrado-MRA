package com.proyecto.proyectointegradomra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.proyecto.proyectointegradomra.navigation.NavigationManager
import com.proyecto.proyectointegradomra.ui.theme.ProyectoIntegradoMRATheme


class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            navController = rememberNavController()
            ProyectoIntegradoMRATheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavigationManager(navController)
                }
            }
        }
    }
}
