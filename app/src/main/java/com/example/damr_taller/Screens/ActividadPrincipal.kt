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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.damr_taller.ui.theme.purpleDark

@Composable
fun ActividadPrincipal(navController: NavController, backgroundColor: Color) {
    var nombre by remember { mutableStateOf("") }
    var nombreAMostrar by remember { mutableStateOf("") }
    var mostrarSaludo by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

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
            keyboardActions = KeyboardActions(
            )
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
    }
}