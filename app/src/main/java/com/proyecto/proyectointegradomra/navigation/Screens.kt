package com.proyecto.proyectointegradomra.navigation

sealed class Screens(val ruta: String) {
    data object StartScreen : Screens("StartView")
    data object SignUpScreen : Screens("SingUpView")
    data object LogInScreen : Screens("LogInView")
    data object HomeScreen : Screens("HomeView")
    data object CreateAdScreen : Screens("CreateAdView")
    data object ProfileScreen : Screens("ProfileView")
    data object CreateScreen : Screens("CreateView")
    data object UpdateAdScreen : Screens("UpdateAdView")

}