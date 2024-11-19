package com.proyecto.proyectointegradomra.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.gson.Gson
import com.proyecto.proyectointegradomra.data.model.Publicacion
import com.proyecto.proyectointegradomra.repository.DataRepository
import com.proyecto.proyectointegradomra.ui.createAd.CreateAdView
import com.proyecto.proyectointegradomra.ui.start.StartView
import com.proyecto.proyectointegradomra.ui.signUp.SingUpView
import com.proyecto.proyectointegradomra.ui.login.LogInView
import com.proyecto.proyectointegradomra.ui.profile.ProfileView
import com.proyecto.proyectointegradomra.ui.create.CreateView
import com.proyecto.proyectointegradomra.ui.home.HomeView
import com.proyecto.proyectointegradomra.ui.updateAd.UpdateAdView
import com.proyecto.proyectointegradomra.ui.updateAd.UpdateAdViewModel

@Composable
fun NavigationManager(
    navController: NavHostController, dataRepository: DataRepository
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
            navController.navigate(Screens.HomeScreen.ruta) {
                // Limpia la pila de navegación para evitar volver a la pantalla de inicio
                popUpTo(Screens.StartScreen.ruta) { inclusive = true }
                launchSingleTop = true
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
            // Pantalla de inicio
            StartView(
                navToSignUp = { navController.navigate(Screens.SignUpScreen.ruta) },
                navToLogIn = { navController.navigate(Screens.LogInScreen.ruta) })
        }
        composable(route = Screens.SignUpScreen.ruta) {
            // Pantalla de registro de usuario
            SingUpView(
                navToLogIn = { navController.navigate(Screens.LogInScreen.ruta) },
                dataRepository = dataRepository
            )
        }
        composable(route = Screens.LogInScreen.ruta) {
            // Pantalla de inicio de sesión
            LogInView(
                navToHome = { navController.navigate(Screens.HomeScreen.ruta) },
                dataRepository = dataRepository
            )
        }
        composable(route = Screens.HomeScreen.ruta) {
            // Pantalla principal para usuarios
            HomeView(navTo = navController, dataRepository = dataRepository)
        }
        composable(route = Screens.ProfileScreen.ruta) {
            // Pantalla de perfil del usuario
            ProfileView(navTo = navController, dataRepository = dataRepository)
        }
        composable(route = Screens.CreateScreen.ruta) {
            // Pantalla de Crear del usuario
            CreateView(navTo = navController, dataRepository = dataRepository)
        }
        composable(route = Screens.CreateAdScreen.ruta) {
            // Pantalla para crear una nueva publicación
            CreateAdView(navTo = navController, dataRepository = dataRepository)
        }
        composable("UpdateAdView/{publicacionJson}") {
            // Pantalla para actualizar una publicación existente, se recibe el objeto Publicacion como JSON
                backStackEntry ->
            val publicacionJson = backStackEntry.arguments?.getString("publicacionJson")
            val publicacion = Gson().fromJson(publicacionJson, Publicacion::class.java)
            UpdateAdView(
                updateAdController = UpdateAdViewModel(publicacion),
                navTo = navController,
                dataRepository = dataRepository
            )
        }
    }
}