package com.proyecto.proyectointegradomra.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.proyecto.proyectointegradomra.view.login.LogInView
import com.proyecto.proyectointegradomra.view.singUp.SingUpView
import com.proyecto.proyectointegradomra.view.home.HomeView
import com.proyecto.proyectointegradomra.view.start.StartScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavigationManager(navController: NavHostController) {

    val auth = remember { FirebaseAuth.getInstance() }
    val currentUser by remember { derivedStateOf { auth.currentUser } }

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            navController.navigate(Screens.HomeScreen.ruta) {
                popUpTo(Screens.StartScreen.ruta) { inclusive = true }
                popUpTo(Screens.LogInScreen.ruta) { inclusive = true }
                popUpTo(Screens.SignUpScreen.ruta) { inclusive = true }
                /*TODO: No permitir volver a pantallas anteriores*/
                launchSingleTop = true
            }
        } else {
            navController.navigate(Screens.StartScreen.ruta) {
                popUpTo(Screens.HomeScreen.ruta) { inclusive = true }
                launchSingleTop = true
            }
        }
    }


    NavHost(navController = navController, startDestination = Screens.StartScreen.ruta) {
        composable(route = Screens.StartScreen.ruta) {
            StartScreen(
                navToSignUp = { navController.navigate(Screens.SignUpScreen.ruta) },
                navToLogIn = { navController.navigate(Screens.LogInScreen.ruta) }
            )
        }
        composable(route = Screens.SignUpScreen.ruta) {
            SingUpView(
                navToHome = navController
            )
        }
        composable(route = Screens.LogInScreen.ruta) {
            LogInView(
                navToHome = navController
            )
        }
        composable(route = Screens.HomeScreen.ruta) {
            HomeView(
               // navTo = navController
            )

        }

    }
}
