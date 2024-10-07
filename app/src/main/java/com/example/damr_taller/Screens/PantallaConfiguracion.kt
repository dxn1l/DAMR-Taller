package com.example.damr_taller.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.damr_taller.ui.theme.DAMRTaller1Theme
import com.example.damr_taller.ui.theme.cremaColor
import com.example.damr_taller.ui.theme.purpleDark

@Composable
fun PantallaConfiguracion(navController: NavHostController, color: Color, onColorChanged: (Color) -> Unit ) {

    var colorFondo by remember { mutableStateOf(color) }

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


