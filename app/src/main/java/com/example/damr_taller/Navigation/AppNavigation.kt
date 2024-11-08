package com.example.damr_taller.Navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.damr_taller.Screens.ActividadPrincipal
import com.example.damr_taller.Screens.PantallaConfiguracion
import com.example.damr_taller.Screens.PantallaInicio
import com.example.damr_taller.Screens.getBackgroundColor

@Composable
fun AppNavigation(startDestination: String = "pantallaInicio") {
    val navController = rememberNavController()
    val context = LocalContext.current
    var backgroundColor by remember { mutableStateOf(getBackgroundColor(context)) }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("pantallaInicio") { PantallaInicio(navController, backgroundColor) }
        composable("mainActivity") { ActividadPrincipal(navController, backgroundColor) }
        composable("pantallaConfiguracion") {
            PantallaConfiguracion(
                navController = navController,
                color = backgroundColor,
                onColorChanged = { backgroundColor = it }
            )
        }
    }
}