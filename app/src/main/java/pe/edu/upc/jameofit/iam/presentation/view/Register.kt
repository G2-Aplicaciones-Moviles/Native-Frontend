package pe.edu.upc.jameofit.iam.presentation.view

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pe.edu.upc.jameofit.MainActivity
import pe.edu.upc.jameofit.R


@Composable
fun Register(recordarPantalla: NavHostController, mainActivity: MainActivity){

    val pref: SharedPreferences = mainActivity.getSharedPreferences("pref_register", Context.MODE_PRIVATE)

    var txtEmail by remember { mutableStateOf("") }
    var txtPass by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {

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
                .padding(top = 80.dp, bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de la aplicación",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 16.dp)
            )

            Text(
                text = "Registrarse",
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = txtEmail,
                modifier = Modifier.padding(vertical = 8.dp),
                label = { Text(text = "Correo electrónico") },
                placeholder = { Text(text = "ejemplo@correo.com") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        tint = Color.Gray,
                        contentDescription = "Icono de correo"
                    )
                },
                onValueChange = { txtEmail = it },
                singleLine = true
            )

            OutlinedTextField(
                value = txtPass,
                modifier = Modifier.padding(vertical = 8.dp),
                label = { Text(text = "Contraseña") },
                placeholder = { Text(text = "Mínimo 6 caracteres") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        tint = Color.Gray,
                        contentDescription = "Icono de contraseña"
                    )
                },
                onValueChange = { txtPass = it },
                singleLine = true
            )

            ElevatedButton(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF099FE1)
                ),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .height(48.dp),
                onClick = {

                    if (txtEmail.isNotEmpty() && txtPass.isNotEmpty()) {
                        if (txtPass.length >= 6) {

                            val editor: SharedPreferences.Editor = pref.edit()
                            editor.putString("email", txtEmail)
                            editor.putString("password", txtPass)
                            editor.putLong("registration_date", System.currentTimeMillis())
                            editor.apply()


                            recordarPantalla.navigate("V4")
                        } else {


                        }
                    }

                }
            ) {
                Text(
                    text = "Crear Cuenta",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }


            Text(
                text = "Al registrarte, aceptas nuestros términos y condiciones",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 16.dp )
                    .padding(horizontal = 20.dp)
            )
        }
    }
}