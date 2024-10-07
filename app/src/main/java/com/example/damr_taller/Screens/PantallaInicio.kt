package com.example.damr_taller.Screens

import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.damr_taller.ui.theme.purpleDark

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