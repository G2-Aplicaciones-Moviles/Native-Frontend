package pe.edu.upc.jameofit.profile.presentation.view

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pe.edu.upc.jameofit.MainActivity

@Composable
fun HealthSetup(recordarPantalla: NavHostController, mainActivity: MainActivity){

    val pref: SharedPreferences = mainActivity.getSharedPreferences("pref_health_profile", Context.MODE_PRIVATE)
    var txtObj by remember { mutableStateOf("") }
    var txtAct by remember { mutableStateOf("") }

    var expandedObj by remember { mutableStateOf(false) }
    var expandedAct by remember { mutableStateOf(false) }

    val objetivos = listOf(
        "Bajar de Peso",
        "Mantener Peso",
        "Ganar Músculo"
    )

    val nivelesActividad = listOf(
        "Sedentario",
        "Ligeramente Activo",
        "Moderadamente Activo",
        "Muy Activo",
        "Extremadamente Activo"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ){
        IconButton(
            onClick = { recordarPantalla.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Regresar",
                tint = Color.Black
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
                .padding(top = 20.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Completa tu información para recibir sugerencias personalizadas",
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(5.dp)
            )

            Box {
                OutlinedTextField(
                    value = txtObj,
                    onValueChange = { },
                    modifier = Modifier.padding(vertical = 8.dp),
                    label = { Text(text = "Objetivo") },
                    placeholder = { Text(text = "Selecciona tu objetivo") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Star,
                            tint = Color.Gray,
                            contentDescription = "Icono de objetivo"
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { expandedObj = !expandedObj }) {
                            Icon(
                                imageVector = if (expandedObj) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Dropdown arrow",
                                tint = Color.Gray
                            )
                        }
                    },
                    readOnly = true,
                    singleLine = true
                )

                DropdownMenu(
                    expanded = expandedObj,
                    onDismissRequest = { expandedObj = false }
                ) {
                    objetivos.forEach { objetivo ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = objetivo,
                                    fontSize = 16.sp
                                )
                            },
                            onClick = {
                                txtObj = objetivo
                                expandedObj = false
                            }
                        )
                    }
                }
            }

            Box {
                OutlinedTextField(
                    value = txtAct,
                    onValueChange = { }, // No editable
                    modifier = Modifier.padding(vertical = 8.dp),
                    label = { Text(text = "Nivel de Actividad") },
                    placeholder = { Text(text = "Selecciona tu nivel de actividad") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            tint = Color.Gray,
                            contentDescription = "Icono de actividad"
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { expandedAct = !expandedAct }) {
                            Icon(
                                imageVector = if (expandedAct) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Dropdown arrow",
                                tint = Color.Gray
                            )
                        }
                    },
                    readOnly = true,
                    singleLine = true
                )

                DropdownMenu(
                    expanded = expandedAct,
                    onDismissRequest = { expandedAct = false }
                ) {
                    nivelesActividad.forEach { nivel ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = nivel,
                                    fontSize = 16.sp
                                )
                            },
                            onClick = {
                                txtAct = nivel
                                expandedAct = false
                            }
                        )
                    }
                }
            }

            Text(
                text = "Recuerda que puedes editar tu perfil más adelante",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(5.dp)
            )

            ElevatedButton(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF099FE1)
                ),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .height(48.dp),
                onClick = {
                    if (txtObj.trim().isNotEmpty() && txtAct.trim().isNotEmpty()) {

                        val editor: SharedPreferences.Editor = pref.edit()

                        editor.putString("objetivo", txtObj.trim())
                        editor.putString("nivel_actividad", txtAct.trim())
                        editor.putLong("health_profile_updated", System.currentTimeMillis())
                        editor.putBoolean("health_profile_completed", true)

                        editor.apply()


                        recordarPantalla.navigate("V6")
                    }
                }
            ) {
                Text(
                    text = "Continuar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}