package com.proyecto.proyectointegradomra.navigation

sealed class Screens(val ruta: String) {
    data object StartScreen : Screens("StartScreen")
    data object SignUpScreen : Screens("SingUpView")
    data object LogInScreen : Screens("LogInView")
    data object HomeScreen : Screens("HomeView")
    data object ProfileScreen : Screens("ProfileView")
    data object FavoritesScreen : Screens("FavoritesView")

}