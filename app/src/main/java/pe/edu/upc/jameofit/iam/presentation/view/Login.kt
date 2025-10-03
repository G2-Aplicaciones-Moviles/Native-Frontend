package pe.edu.upc.jameofit.iam.presentation.view

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pe.edu.upc.jameofit.MainActivity
import pe.edu.upc.jameofit.R
import pe.edu.upc.jameofit.ui.theme.JameoBlue
import pe.edu.upc.jameofit.ui.theme.JameoGreen


@Composable
fun Login(recordarPantalla: NavHostController, mainActivity: MainActivity){

    val pref: SharedPreferences = mainActivity.getSharedPreferences("pref1", Context.MODE_PRIVATE)
    val prefRegister: SharedPreferences = mainActivity.getSharedPreferences("pref_register", Context.MODE_PRIVATE)

    val check: Boolean = pref.getBoolean("check", false)
    val email: String = pref.getString("email", "")!!
    val password: String = pref.getString("pas", "")!!


    val registeredEmail: String = prefRegister.getString("email", "")!!
    val registeredPassword: String = prefRegister.getString("password", "")!!

    var txtEmail by remember { mutableStateOf(email) }
    var txtPass by remember { mutableStateOf(password) }
    var chk by remember { mutableStateOf(check) }

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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de la aplicación",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 16.dp)
            )

            Text(
                text = "Iniciar Sesión",
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
                        contentDescription = "Icono de usuario"
                    )
                },
                onValueChange = { txtEmail = it },
                singleLine = true
            )

            OutlinedTextField(
                value = txtPass,
                modifier = Modifier.padding(vertical = 8.dp),
                label = { Text(text = "Contraseña") },
                placeholder = { Text(text = "Ingresa tu contraseña") },
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
                    val editor: SharedPreferences.Editor = pref.edit()


                    val isValidLogin = (txtEmail == registeredEmail && txtPass == registeredPassword) // Credenciales de registro

                    if (isValidLogin && txtEmail.isNotEmpty() && txtPass.isNotEmpty()) {
                        if (chk) {
                            editor.putString("usu", txtEmail)
                            editor.putString("pas", txtPass)
                            editor.putBoolean("check", true)
                        } else {
                            editor.putString("usu", "")
                            editor.putString("pas", "")
                            editor.putBoolean("check", false)
                        }

                        editor.apply()
                        recordarPantalla.navigate("V7")
                    }

                }
            ) {
                Text(
                    text = "Ingresar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Switch(
                    checked = chk,
                    colors = SwitchDefaults.colors(

                        checkedTrackColor = JameoBlue,
                        checkedThumbColor = Color.White,
                        checkedBorderColor = Color.Transparent,
                        checkedIconColor  = JameoGreen,


                        uncheckedTrackColor = MaterialTheme.colorScheme.outlineVariant,
                        uncheckedThumbColor = Color.White,
                        uncheckedBorderColor = Color.Transparent,
                        uncheckedIconColor  = MaterialTheme.colorScheme.onSurfaceVariant),
                    onCheckedChange = { chk = it },
                    thumbContent = if (chk) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Activado",
                                tint = JameoGreen,
                                modifier = Modifier.size(SwitchDefaults.IconSize)
                            )
                        }
                    } else {
                        {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Desactivado",
                                modifier = Modifier.size(SwitchDefaults.IconSize)
                            )
                        }
                    }
                )
                Text(
                    text = "Guardar Credenciales",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
            Spacer(Modifier.height(8.dp))

            Text(
                text = "Olvidé mi contraseña",
                color = MaterialTheme.colorScheme.error,     // rojo del tema; o usa Color(0xFFD32F2F)
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textDecoration = TextDecoration.Underline,   // opcional para que parezca link
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clickable { recordarPantalla.navigate("V8") }
            )
        }
    }
}