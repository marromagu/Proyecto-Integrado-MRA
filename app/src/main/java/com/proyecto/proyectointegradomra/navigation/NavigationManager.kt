package com.proyecto.proyectointegradomra.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.proyecto.proyectointegradomra.ui.login.LogInView
import com.proyecto.proyectointegradomra.ui.home.OfertanteHomeView
import com.proyecto.proyectointegradomra.ui.home.DemandanteHomeView
import com.proyecto.proyectointegradomra.ui.start.StartScreen
import com.proyecto.proyectointegradomra.data.model.TipoUsuario
import com.proyecto.proyectointegradomra.repository.DataRepository
import com.proyecto.proyectointegradomra.ui.favorites.FavoritesView
import com.proyecto.proyectointegradomra.ui.profile.ProfileView
import com.proyecto.proyectointegradomra.ui.signUp.SingUpView

@Composable
fun NavigationManager(navController: NavHostController, dataRepository: DataRepository) {
    // Observa el usuario actual y lanza el efecto una vez cuando se cargue
    val usuario by dataRepository.obtenerUsuarioActual().observeAsState(null)

    // Llama a cargar el usuario una vez al iniciar la composición
    LaunchedEffect(Unit) {
        dataRepository.cargarUsuario()
    }

    // Navegación en función del tipo de usuario
    LaunchedEffect(usuario) {
        usuario?.let {
            when (it.type) {
                TipoUsuario.OFERTANTE -> {
                    navController.navigate(Screens.OfertantesHomeScreen.ruta) {
                        popUpTo(Screens.StartScreen.ruta) { inclusive = true }
                        launchSingleTop = true
                    }
                }

                TipoUsuario.DEMANDANTE -> {
                    navController.navigate(Screens.DemandantesHomeScreen.ruta) {
                        popUpTo(Screens.StartScreen.ruta) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        } ?: run {
            navController.navigate(Screens.StartScreen.ruta) {
                popUpTo(Screens.StartScreen.ruta) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(navController = navController, startDestination = Screens.StartScreen.ruta) {
        composable(route = Screens.StartScreen.ruta) {
            StartScreen(navToSignUp = { navController.navigate(Screens.SignUpScreen.ruta) },
                navToLogIn = { navController.navigate(Screens.LogInScreen.ruta) })
        }
        composable(route = Screens.SignUpScreen.ruta) {
            SingUpView(navToHome = {
                when (usuario?.type) {
                    TipoUsuario.DEMANDANTE -> navController.navigate(Screens.DemandantesHomeScreen.ruta)
                    TipoUsuario.OFERTANTE -> navController.navigate(Screens.OfertantesHomeScreen.ruta)
                    null -> navController.navigate(Screens.StartScreen.ruta)
                }
            }, dataRepository = dataRepository)
        }
        composable(route = Screens.LogInScreen.ruta) {
            LogInView(navToHome = {
                when (usuario?.type) {
                    TipoUsuario.DEMANDANTE -> navController.navigate(Screens.DemandantesHomeScreen.ruta)
                    TipoUsuario.OFERTANTE -> navController.navigate(Screens.OfertantesHomeScreen.ruta)
                    null -> navController.navigate(Screens.StartScreen.ruta)
                }
            }, dataRepository = dataRepository)
        }
        composable(route = Screens.OfertantesHomeScreen.ruta) {
            OfertanteHomeView(navTo = navController, dataRepository = dataRepository)
        }
        composable(route = Screens.DemandantesHomeScreen.ruta) {
            DemandanteHomeView(navTo = navController, dataRepository = dataRepository)
        }
        composable(route = Screens.ProfileScreen.ruta) {
            ProfileView(navTo = navController, dataRepository = dataRepository)
        }
        composable(route = Screens.FavoritesScreen.ruta) {
            FavoritesView(navTo = navController, dataRepository = dataRepository)
        }
    }
}

