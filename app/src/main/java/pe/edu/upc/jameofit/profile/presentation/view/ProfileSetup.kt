package pe.edu.upc.jameofit.profile.presentation.view

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileSetup(
    onNext: () -> Unit,
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val pref: SharedPreferences = remember {
        context.getSharedPreferences("pref_profile", Context.MODE_PRIVATE)
    }

    var txtNom by remember { mutableStateOf("") }
    var txtEda by remember { mutableStateOf("") }
    var txtPes by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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

            OutlinedTextField(
                value = txtNom,
                modifier = Modifier.padding(vertical = 8.dp),
                label = { Text(text = "Nombre") },
                placeholder = { Text(text = "Ingresa tu nombre") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Face,
                        tint = Color.Gray,
                        contentDescription = "Icono de usuario"
                    )
                },
                onValueChange = { txtNom = it },
                singleLine = true
            )

            OutlinedTextField(
                value = txtEda,
                modifier = Modifier.padding(vertical = 8.dp),
                label = { Text(text = "Edad") },
                placeholder = { Text(text = "Ingresa tu edad") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        tint = Color.Gray,
                        contentDescription = "Icono de edad"
                    )
                },
                onValueChange = { txtEda = it },
                singleLine = true
            )

            OutlinedTextField(
                value = txtPes,
                modifier = Modifier.padding(vertical = 8.dp),
                label = { Text(text = "Peso (kg)") },
                placeholder = { Text(text = "Ingresa tu peso") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        tint = Color.Gray,
                        contentDescription = "Icono de peso"
                    )
                },
                onValueChange = { txtPes = it },
                singleLine = true
            )

            Text(
                text = "Recuerda que puedes editar tu perfil más adelante",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            ElevatedButton(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF099FE1)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(48.dp),
                onClick = {
                    if (txtNom.trim().isNotEmpty()) {
                        pref.edit()
                            .putString("nombre", txtNom.trim())
                            .putString("edad", txtEda.trim())
                            .putString("peso", txtPes.trim())
                            .putLong("profile_updated", System.currentTimeMillis())
                            .putBoolean("profile_completed", true)
                            .apply()
                        onNext()
                    }
                }
            ) {
                Text(
                    text = "Continuar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}