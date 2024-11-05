package com.proyecto.proyectointegradomra.view.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.proyecto.proyectointegradomra.firebase.database.AuthController
import com.proyecto.proyectointegradomra.ui.theme.ColorDeFondo
import com.proyecto.proyectointegradomra.view.BottomNavigationBar
import com.proyecto.proyectointegradomra.view.Logo

@Composable
fun DemandanteHomeView(
    authController: AuthController = viewModel(),
    navTo: NavHostController
) {
    val miUsuario by authController.usuario.observeAsState()
    Scaffold(bottomBar = { BottomNavigationBar(navController = navTo) }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(ColorDeFondo)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Logo()
            Text(text = "Demandante Home ${miUsuario?.nombre} y ${miUsuario?.tipo}")
        }
    }
}
