package com.example.damr_taller

import android.icu.util.Calendar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.damr_taller.ui.theme.DAMRTaller1Theme


class PantallaInicio : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DAMRTaller1Theme {
                    AppNavigation(
                    )
                }
            }
        }
    }

val cremaColor = Color(0xFFF5F5DC)
val purpleDark = Color(0xFF4B0082)

@Composable
fun AppNavigation(startDestination: String = "pantallaInicio") {
    val navController = rememberNavController()
    val defaultColor = MaterialTheme.colorScheme.background
    var backgroundColor by remember { mutableStateOf(defaultColor) }

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



@Composable
fun PantallaInicio(navController: NavHostController, backgroundColor: Color){

    val context = LocalContext.current
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val saludo = when (currentHour) {
        in 0..11 -> "Buenos días"
        in 12..18 -> "Buenas tardes"
        else -> "Buenas noches"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = saludo,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = textColor(backgroundColor)
        )
        Button(onClick = { navController.navigate("mainActivity") },
            modifier = Modifier.clip(RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = purpleDark)
            )
            {
            Text("Entrar")
        }
    }
}

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

@Composable
fun PantallaConfiguracion(navController: NavHostController, color: Color, onColorChanged: (Color) -> Unit ) {

    var colorFondo by remember {mutableStateOf(color) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFondo)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Selecciona un color de fondo",
            color = textColor(color))



        ColorSelection(onColorSelected = {colorFondo = it
            onColorChanged(it)})

        Button(onClick = { navController.navigate("pantallaInicio") },
            modifier = Modifier.clip(RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = purpleDark)) {
            Text("Volver a la pantalla de inicio")

        }

    }
}


@Composable
fun ColorSelection(onColorSelected: (Color) -> Unit) {

    val colors = listOf(
        Color.Red, Color.Green, Color.Blue,
        Color.Yellow, Color.Cyan, Color.Magenta,
        Color.LightGray, Color.Black, Color.Gray
    )

    Surface(color = cremaColor,
        shape = RoundedCornerShape(16.dp)
    ){
    Column(modifier = Modifier.padding(16.dp))  {
        colors.chunked(3).forEach { rowColors ->
            Row {
                rowColors.forEach { color ->
                    ColorButton(color = color, onColorSelected = onColorSelected)
                }
            }
        }
    }
}
}

@Composable
fun ColorButton(color: Color, onColorSelected: (Color) -> Unit) {
    Button(
        onClick = { onColorSelected(color) },
        modifier = Modifier.size(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
    }
}

@Composable
fun textColor(backgroundColor: Color): Color {
    return if (backgroundColor == Color.Black) Color.White else Color.Black
}



@Preview(showBackground = true)
@Composable
fun PantallaInicioPreview() {
    DAMRTaller1Theme {
        PantallaInicio(rememberNavController(), MaterialTheme.colorScheme.background)
    }
}