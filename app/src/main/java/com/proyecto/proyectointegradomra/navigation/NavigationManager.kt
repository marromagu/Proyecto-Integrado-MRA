package com.proyecto.proyectointegradomra.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.proyecto.proyectointegradomra.view.login.LogInView
import com.proyecto.proyectointegradomra.view.home.OfertanteHomeView
import com.proyecto.proyectointegradomra.view.home.DemandanteHomeView
import com.proyecto.proyectointegradomra.view.start.StartScreen
import com.google.firebase.auth.FirebaseAuth
import com.proyecto.proyectointegradomra.Data.TipoUsuario
import com.proyecto.proyectointegradomra.authentication.AuthController
import com.proyecto.proyectointegradomra.view.favorites.FavoritesView
import com.proyecto.proyectointegradomra.view.profile.ProfileView
import com.proyecto.proyectointegradomra.view.signUp.SingUpView

@Composable
fun NavigationManager(
    navController: NavHostController, authController: AuthController = viewModel()
) {
    val auth = remember { FirebaseAuth.getInstance() }
    val currentUser by remember { derivedStateOf { auth.currentUser } }
    val usuario by authController.usuario.observeAsState()

    if (currentUser != null) {
        LaunchedEffect(currentUser) {
            authController.cargarUsuario()
        }
    }

    LaunchedEffect(usuario) {
        when {
            usuario == null -> {
                navController.navigate(Screens.StartScreen.ruta) {
                    popUpTo(Screens.HomeScreen.ruta) { inclusive = true }
                    launchSingleTop = true
                }
            }

            usuario?.tipo == TipoUsuario.OFERTANTE -> {
                navController.navigate(Screens.OfertantesHomeScreen.ruta) {
                    popUpTo(Screens.StartScreen.ruta) { inclusive = true }
                    launchSingleTop = true
                }
            }

            usuario?.tipo == TipoUsuario.DEMANDANTE -> {
                navController.navigate(Screens.DemandantesHomeScreen.ruta) {
                    popUpTo(Screens.StartScreen.ruta) { inclusive = true }
                    launchSingleTop = true
                }
            }

            else -> {
                // navController.navigate(Screens.ErrorScreen.ruta)
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
                when (usuario?.tipo) {
                    TipoUsuario.DEMANDANTE -> navController.navigate(Screens.DemandantesHomeScreen.ruta)
                    TipoUsuario.OFERTANTE -> navController.navigate(Screens.OfertantesHomeScreen.ruta)
                    null -> navController.navigate(Screens.StartScreen.ruta)
                    TipoUsuario.OTRO -> TODO()
                }
            })
        }
        composable(route = Screens.LogInScreen.ruta) {
            LogInView(navToHome = {
                when (usuario?.tipo) {
                    TipoUsuario.DEMANDANTE -> navController.navigate(Screens.DemandantesHomeScreen.ruta)
                    TipoUsuario.OFERTANTE -> navController.navigate(Screens.OfertantesHomeScreen.ruta)
                    null -> navController.navigate(Screens.StartScreen.ruta)
                    TipoUsuario.OTRO -> TODO()
                }
            })
        }
        composable(route = Screens.OfertantesHomeScreen.ruta) {
            OfertanteHomeView(navTo = navController)
        }
        composable(route = Screens.DemandantesHomeScreen.ruta) {
            DemandanteHomeView(navTo = navController)
        }
        composable(route = Screens.ProfileScreen.ruta) {
            ProfileView(navTo = navController)
        }
        composable(route = Screens.FavoritesScreen.ruta) {
            FavoritesView(navTo = navController)
        }
    }
}
