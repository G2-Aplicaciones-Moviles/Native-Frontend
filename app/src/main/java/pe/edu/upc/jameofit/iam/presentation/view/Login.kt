package pe.edu.upc.jameofit.iam.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import pe.edu.upc.jameofit.R
import pe.edu.upc.jameofit.iam.presentation.viewmodel.AuthViewModel
import pe.edu.upc.jameofit.shared.presentation.components.ErrorSnackbarHost
import pe.edu.upc.jameofit.shared.presentation.components.showErrorOnce
import pe.edu.upc.jameofit.ui.theme.JameoBlue
import pe.edu.upc.jameofit.ui.theme.JameoGreen


@Composable
fun Login(
    viewmodel: AuthViewModel,
    goToRegister: () -> Unit,
    onLoginSuccess: (Long) -> Unit,
    goToForgotPassword: () -> Unit
) {

    val user by viewmodel.user.collectAsState()
    val loginSuccess by viewmodel.loginSuccess.collectAsState()
    val currentUserId by viewmodel.currentUserId.collectAsState()
    val errorMessage by viewmodel.errorMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // “Recordar credenciales” (solo UI por ahora)
    var chk by remember { mutableStateOf(false) }

    // Navegación de éxito
    LaunchedEffect(loginSuccess) {
        if (loginSuccess == true) {
            onLoginSuccess(currentUserId!!)
            viewmodel.resetLoginSuccess()
        }
    }

    // Errores → Snackbar
    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            scope.launch { snackbarHostState.showErrorOnce(errorMessage!!) }
            viewmodel.resetErrorMessage()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
                .padding(top = 80.dp, bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

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
                value = user.username,
                modifier = Modifier.padding(vertical = 8.dp),
                label = { Text(text = "Nombre de usuario") },
                placeholder = { Text(text = "JameoFit") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        tint = Color.Gray,
                        contentDescription = "Icono de usuario"
                    )
                },
                onValueChange = { viewmodel.updateUsername(it) },
                singleLine = true
            )

            OutlinedTextField(
                value = user.password,
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
                onValueChange = { viewmodel.updatePassword(it) },
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
                    viewmodel.login()
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
                        checkedIconColor = JameoGreen,


                        uncheckedTrackColor = MaterialTheme.colorScheme.outlineVariant,
                        uncheckedThumbColor = Color.White,
                        uncheckedBorderColor = Color.Transparent,
                        uncheckedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
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
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clickable { goToForgotPassword() }
            )

            Spacer(Modifier.height(24.dp))

            Row {
                Text("¿No tienes cuenta aún? ")
                Text(
                    text = "Regístrate",
                    color = JameoBlue,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { goToRegister() }
                )
            }
        }
        ErrorSnackbarHost(hostState = snackbarHostState)
    }
}