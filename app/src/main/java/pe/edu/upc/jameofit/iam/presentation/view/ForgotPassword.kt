package pe.edu.upc.jameofit.iam.presentation.view

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
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pe.edu.upc.jameofit.MainActivity


@Composable
fun ForgotPassword(recordarPantalla: NavHostController, mainActivity: MainActivity){

    val prefRegister: SharedPreferences = mainActivity.getSharedPreferences("pref_register", Context.MODE_PRIVATE)
    val registeredEmail: String = prefRegister.getString("email", "")!!

    var txtRecoveryEmail by remember { mutableStateOf("") }


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
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
            .padding(top = 80.dp, bottom = 30.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Recuperación de Contraseña",
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 32.dp)
            )


            Text(text = "Ingresa tu correo electrónico " +
                    "y te enviaremos un enlace para reestablecer tu contraseña.",
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(5.dp)
            )

            OutlinedTextField(
                value = txtRecoveryEmail,
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
                onValueChange = { txtRecoveryEmail = it },
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



                    val isValidLogin = (txtRecoveryEmail == registeredEmail)

                    if (isValidLogin && txtRecoveryEmail.isNotEmpty()) {
                        recordarPantalla.navigate("V7")
                    }

                }
            ) {
                Text(
                    text = "Enviar instrucciones",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }

        }
    }
}