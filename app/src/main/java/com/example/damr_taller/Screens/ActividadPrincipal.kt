package com.example.damr_taller.Screens

import android.annotation.SuppressLint
import android.content.SharedPreferences
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.damr_taller.FirebaseDatabase.FirebaseNameRepository
import com.example.damr_taller.FirebaseDatabase.Name
import com.example.damr_taller.LocalDatabase.NameDatabaseHelper
import com.example.damr_taller.ui.theme.purpleDark
import com.example.damr_taller.ui.theme.purpleLight
import kotlinx.coroutines.launch
import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@SuppressLint("CommitPrefEdits")
@Composable
fun ActividadPrincipal(navController: NavController, backgroundColor: Color) {

    val context = LocalContext.current
    val dbHelper = NameDatabaseHelper(context)
    val repository = FirebaseNameRepository()
    val scope = rememberCoroutineScope()
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val lastSavedName = sharedPreferences.getString("lastSavedName", "")
    var nombre by remember { mutableStateOf("") }
    var nombreAMostrar by remember { mutableStateOf("") }
    var mostrarSaludo by remember { mutableStateOf(false) }
    var nombresList by remember { mutableStateOf<Set<String>>(emptySet()) }
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

        if (!lastSavedName.isNullOrEmpty()) {
            Text(
                text = "Último usuario agregado: $lastSavedName",
                modifier = Modifier.padding(16.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = textColor(backgroundColor)
            )
        }

        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (nombre.isNotBlank() && nombre.matches(Regex("^[a-zA-Z\u00C0-\u017F ]+\$"))) {
                    scope.launch {
                        repository.nameExits(nombre, { existsInFirebase ->
                            if (existsInFirebase || dbHelper.nameExists(nombre)) {
                                nombreAMostrar = "El nombre ya existe en la base de datos"
                            } else {
                                repository.addName(Name(name = nombre), {
                                    val result = dbHelper.addName(nombre)
                                    if (result != -1L) {
                                        nombreAMostrar = nombre
                                        mostrarSaludo = true
                                        editor.putString("lastSavedName", nombre)
                                        editor.apply()
                                    } else {
                                        nombreAMostrar =
                                            "Error al guardar el nombre en la base de datos local"
                                    }
                                    keyboardController?.hide()
                                }, {
                                    nombreAMostrar = "Error al guardar el nombre en Firebase"
                                    mostrarSaludo = true
                                    keyboardController?.hide()
                                })
                            }
                            mostrarSaludo = true
                        }, {
                            nombreAMostrar = "Error al verificar los nombres en Firebase"
                            mostrarSaludo = true
                        })
                    }
                } else {
                    nombreAMostrar =
                        if (nombre.isBlank()) "No has ingresado un nombre" else "Solo se permiten letras"
                }
                mostrarSaludo = true
                keyboardController?.hide()
            },
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, Color.Black, RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Green , contentColor = Color.Black)
        ) {
            Text("Guardar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Button(
            onClick = {
                scope.launch {
                    repository.deleteAllNames({
                        dbHelper.deleteAllNames()
                        editor.remove("lastSavedName")
                        editor.apply()
                        nombreAMostrar = "Todos los usuarios han sido borrados"
                        mostrarSaludo = true
                    }, {
                        nombreAMostrar = "Error al borrar los nombres"
                        mostrarSaludo = true
                    })
                }
            },
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, Color.Black, RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red , contentColor = Color.Black)
        ) {
            Text("Borrar todos los usuarios", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Button(
            onClick = {
                scope.launch {
                    repository.getNames({ names ->
                        val localNames = dbHelper.getAllNames()
                        nombresList = (names.map { it.name } + localNames).toSet()
                    }, {
                        nombreAMostrar = "Error al obtener los nombres"
                        mostrarSaludo = true
                    })
                }
            },
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, Color.Black, RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = purpleDark , contentColor = Color.Black)
        ) {
            Text("Mostrar todos los nombres", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

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

        LazyColumn(

            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(8.dp)
        ) {

        items(nombresList.toList()) { nombre ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Cyan , contentColor = Color.Black),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = nombre,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,

                        )
                }
            }
        }
    }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("pantallaConfiguracion") },
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, Color.Black, RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray , contentColor = Color.Black)) {
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
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, Color.Black, RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = purpleDark , contentColor = Color.Black)
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


@Suppress("DEPRECATION")
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

