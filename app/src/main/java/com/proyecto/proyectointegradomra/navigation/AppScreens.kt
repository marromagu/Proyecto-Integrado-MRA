package com.proyecto.proyectointegradomra.navigation

sealed class AppScreens(val ruta: String){
    data object StartScreen: AppScreens("start_screen")
    data object LogInScreen: AppScreens("login_screen")
    data object SignUpScreen: AppScreens("signup_screen")
//    data object HomeScreen: AppScreens("home_screen")
}