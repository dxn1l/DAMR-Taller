package com.example.damr_taller.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.damr_taller.ui.theme.purpleDark
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ActividadPrincipal(navController: NavController, backgroundColor: Color) {
    var nombre by remember { mutableStateOf("") }
    var nombreAMostrar by remember { mutableStateOf("") }
    var mostrarSaludo by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    var mostrandoProgreso by remember { mutableStateOf(false) }
    var progreso by remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions()
        )

        Button(onClick = {
            if (nombre.isNotBlank() && nombre.matches(Regex("^[a-zA-Z\u00C0-\u017F ]+\$"))) {
                nombreAMostrar = nombre
            } else {
                nombreAMostrar = if (nombre.isBlank()) "No has ingresado un nombre" else "Solo se permiten letras"
            }
            mostrarSaludo = true
            keyboardController?.hide()
        },
            modifier = Modifier.clip(RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = purpleDark)) {
            Text("Guardar")
        }

        if (mostrarSaludo) {
            Text(
                text = "Hola. $nombreAMostrar ",
                modifier = Modifier.padding(25.dp),
                color = textColor(backgroundColor)
            )
        }

        Button(onClick = { navController.navigate("pantallaConfiguracion") },
            modifier = Modifier.clip(RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = purpleDark))  {
            Text("Siguiente")
        }


        Button(
            onClick = {
                mostrandoProgreso = true
                progreso = 0f
                scope.launch {
                    simularOperacionRed { nuevoProgreso ->
                        progreso = nuevoProgreso
                    }
                    mostrandoProgreso = false
                }
            },
            modifier = Modifier.clip(RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = purpleDark)
        ) {
            Text("Iniciar tarea en segundo plano")
        }

        if (mostrandoProgreso) {
            LinearProgressIndicator(
                progress = progreso,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(0.3f)
            )
        }


    }
}


suspend fun simularOperacionRed(onProgresoActualizado: (Float) -> Unit) {
    val totalPasos = 10
    for (i in 1..totalPasos) {
        delay(500)
        val progresoActual = i / totalPasos.toFloat()
        onProgresoActualizado(progresoActual)
    }
}

