import android.os.AsyncTask
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.damr_taller.Screens.textColor
import com.example.damr_taller.ui.theme.purpleDark
import com.example.damr_taller.ui.theme.purpleLight

@Composable
fun ActividadPrincipal(navController: NavController, backgroundColor: Color) {
    var nombre by remember { mutableStateOf("") }
    var nombreAMostrar by remember { mutableStateOf("") }
    var mostrarSaludo by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    var mostrandoProgreso by remember { mutableStateOf(false) }
    var progreso by remember { mutableStateOf(0f) }
    var snackBarVisible by remember { mutableStateOf(false) }
    val progresoAnimado by animateFloatAsState(
        targetValue = progreso,
        animationSpec = tween(durationMillis = 500)
    )

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

        Spacer(modifier = Modifier.height(16.dp))

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
            colors = ButtonDefaults.buttonColors(containerColor = purpleDark)
        ) {
            Text("Guardar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        if (mostrarSaludo) {
            Text(
                text = "Hola. $nombreAMostrar ",
                modifier = Modifier.padding(16.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = textColor(backgroundColor)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("pantallaConfiguracion") },
            modifier = Modifier.clip(RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = purpleDark)) {
            Text("Siguiente", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                mostrandoProgreso = true
                progreso = 0f
                SimularOperacionRedAsyncTask(
                    onProgresoActualizado = { nuevoProgreso ->
                        progreso = nuevoProgreso
                    },
                    onFinalizado = {
                        mostrandoProgreso = false
                        snackBarVisible = true
                    }
                ).execute()
            },
            modifier = Modifier.clip(RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = purpleDark)
        ) {
            Text("Iniciar tarea en segundo plano", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (mostrandoProgreso) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    progress = progresoAnimado,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(16.dp),
                    color = purpleLight,
                    strokeWidth = 8.dp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${(progresoAnimado * 100).toInt()}%",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
        if (snackBarVisible) {
            Snackbar(
                action = {
                    TextButton(onClick = { snackBarVisible = false }) {
                        Text("Cerrar")
                    }
                }
            ) {
                Text("Proceso en segundo plano finalizado")
            }
        }
    }
}


class SimularOperacionRedAsyncTask(
    private val onProgresoActualizado: (Float) -> Unit,
    private val onFinalizado: () -> Unit
) : AsyncTask<Void, Float, Void>() {

    override fun doInBackground(vararg params: Void?): Void? {
        val totalPasos = 10
        for (i in 1..totalPasos) {
            try {
                Thread.sleep(500)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            val progresoActual = i / totalPasos.toFloat()
            publishProgress(progresoActual)
        }
        Thread.sleep(3000)
        return null
    }

    override fun onProgressUpdate(vararg values: Float?) {
        super.onProgressUpdate(*values)
        values[0]?.let {
            onProgresoActualizado(it)
        }
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        onFinalizado()
    }
}

