# Taller

Este proyecto de Android, desarrollado con Kotlin y Jetpack Compose, ofrece una interfaz de usuario interactiva con tres pantallas : 'Pantalla de Inicio' , 'Actividad Principal' y 'Pantalla de Configuración'

## Link al repositorio

* https://github.com/dxn1l/DAMR-Taller

## Características

**1. Pantalla de Inicio:**

* Muestra un saludo personalizado que depende de la hora en la que se encuentra el dispositivo del usuario.
* Incluye un botón para navegar a la Actividad Principal.

**2. Actividad Principal:**

* Muestra un Texto que indica el nombre del ultimo usuario agregado y este nombre se guarda en un sharedPreferences.
* Permite al usuario ingresar su nombre en un campo de texto y este se muestra en un TextView.
* Dispone de un botón para guardar el nombre ingresado.
* Ofrece un botón para navegar a la Pantalla de Configuración.
* Incorpora una funcionalidad para simular una operación en segundo plano que muestra un progreso mediante un CircularProgressIndicator y un porcentaje en pantalla.
* Muestra un Snackbar al finalizar la operación en segundo plano.
* Los nombres se guardan en Firebase y en una base de datos local SQLite.
* Se Dispone de un botón para ver los nombres guardados.
* Se dispone de un botón para eliminar los nombres guardados.

**3. Pantalla de Configuración:**

* Brinda opciones para personalizar el color de fondo de la aplicación , se ofrece un total de 9 colores.
* Incluye un botón para volver a la Pantalla de Inicio.
* El color seleccionado se guarda en SharedPreferences.

## Tecnologías utilizadas

* Kotlin
* Jetpack Compose
* Firebase Firestore Database
* SQLite


## Instalación

Para ejecutar este proyecto, sigue estos pasos:

1. Clona el repositorio en tu máquina local.
2. Abre el proyecto en Android Studio.
3. Compila y ejecuta la aplicación en un emulador o dispositivo Android.

## Uso

1. **Pantalla de Inicio:** Observa el saludo personalizado y presiona el botón para ir a la Actividad Principal.
2. **Actividad Principal:** Ingresa tu nombre en el campo de texto y presiona "Guardar". Tu nombre se mostrará en pantalla. Presiona el botón para ir a la Pantalla de Configuración. Puedes iniciar una tarea en segundo plano, que mostrará un progreso animado y un mensaje al finalizar. Presiona el botón para ir a la Pantalla de Configuración. Puedes mostrar/borrar los nombres guardados.
3. **Pantalla de Configuración:** Selecciona un color de fondo y presiona el botón para volver a la Pantalla de Inicio.