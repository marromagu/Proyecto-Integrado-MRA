package com.proyecto.proyectointegradomra.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.proyecto.proyectointegradomra.data.model.TipoUsuario
import com.proyecto.proyectointegradomra.repository.DataRepository
import com.proyecto.proyectointegradomra.ui.start.StartView
import com.proyecto.proyectointegradomra.ui.signUp.SingUpView
import com.proyecto.proyectointegradomra.ui.login.LogInView
import com.proyecto.proyectointegradomra.ui.home.DemandanteHomeView
import com.proyecto.proyectointegradomra.ui.home.OfertanteHomeView
import com.proyecto.proyectointegradomra.ui.profile.ProfileView
import com.proyecto.proyectointegradomra.ui.favorites.FavoritesView

@Composable
fun NavigationManager(
    navController: NavHostController,
    dataRepository: DataRepository
) {
    // Observa los cambios en el usuario actual para reaccionar dinámicamente ante la autenticación
    val usuario by dataRepository.obtenerUsuarioActual().observeAsState(null)

    // Cargar los datos del usuario desde Firestore al iniciar la composición
    LaunchedEffect(Unit) {
        dataRepository.cargarUsuario()
    }

    // Control de navegación en función del tipo de usuario autenticado
    LaunchedEffect(usuario) {
        usuario?.let {
            when (it.type) {
                TipoUsuario.OFERTANTE -> {
                    navController.navigate(Screens.OfertantesHomeScreen.ruta) {
                        // Limpia la pila de navegación para evitar volver a la pantalla de inicio
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
            // Redirige a la pantalla de inicio si el usuario es nulo (no autenticado)
            navController.navigate(Screens.StartScreen.ruta) {
                popUpTo(Screens.StartScreen.ruta) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    // Configuración del NavHost con las rutas disponibles en la aplicación
    NavHost(navController = navController, startDestination = Screens.StartScreen.ruta) {
        composable(route = Screens.StartScreen.ruta) {
            StartView(
                navToSignUp = { navController.navigate(Screens.SignUpScreen.ruta) },
                navToLogIn = { navController.navigate(Screens.LogInScreen.ruta) }
            )
        }
        composable(route = Screens.SignUpScreen.ruta) {
            SingUpView(
                navToHome = {
                    // Navega al inicio adecuado según el tipo de usuario registrado
                    when (usuario?.type) {
                        TipoUsuario.DEMANDANTE -> navController.navigate(Screens.DemandantesHomeScreen.ruta)
                        TipoUsuario.OFERTANTE -> navController.navigate(Screens.OfertantesHomeScreen.ruta)
                        else -> navController.navigate(Screens.StartScreen.ruta)
                    }
                },
                dataRepository = dataRepository
            )
        }
        composable(route = Screens.LogInScreen.ruta) {
            LogInView(
                navToHome = {
                    // Navega al inicio adecuado según el tipo de usuario autenticado
                    when (usuario?.type) {
                        TipoUsuario.DEMANDANTE -> navController.navigate(Screens.DemandantesHomeScreen.ruta)
                        TipoUsuario.OFERTANTE -> navController.navigate(Screens.OfertantesHomeScreen.ruta)
                        else -> navController.navigate(Screens.StartScreen.ruta)
                    }
                },
                dataRepository = dataRepository
            )
        }
        composable(route = Screens.OfertantesHomeScreen.ruta) {
            // Pantalla principal para usuarios ofertantes
            OfertanteHomeView(navTo = navController, dataRepository = dataRepository)
        }
        composable(route = Screens.DemandantesHomeScreen.ruta) {
            // Pantalla principal para usuarios demandantes
            DemandanteHomeView(navTo = navController, dataRepository = dataRepository)
        }
        composable(route = Screens.ProfileScreen.ruta) {
            // Pantalla de perfil del usuario
            ProfileView(navTo = navController, dataRepository = dataRepository)
        }
        composable(route = Screens.FavoritesScreen.ruta) {
            // Pantalla de favoritos del usuario
            FavoritesView(navTo = navController, dataRepository = dataRepository)
        }
    }
}