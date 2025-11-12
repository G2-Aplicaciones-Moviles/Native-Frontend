package pe.edu.upc.jameofit.iam.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
    viewModel: AuthViewModel,
    goToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    goToForgotPassword: () -> Unit
) {
    val user by viewModel.user.collectAsState()
    val loginSuccess by viewModel.loginSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var chk by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    // Navegación de éxito
    LaunchedEffect(loginSuccess) {
        if (loginSuccess == true) {
            onLoginSuccess()
            viewModel.resetLoginSuccess()
        }
    }

    // Mostrar errores una sola vez
    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            scope.launch { snackbarHostState.showErrorOnce(errorMessage!!) }
            viewModel.resetErrorMessage()
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
                onValueChange = { viewModel.updateUsername(it) },
                singleLine = true,
                isError = !errorMessage.isNullOrBlank() && user.username.isBlank()
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
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(id = if (passwordVisible) R.drawable.visibility_24px else R.drawable.visibility_off_24px),
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = Color.Unspecified
                        )
                    }
                },
                onValueChange = { viewModel.updatePassword(it) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = !errorMessage.isNullOrBlank() && user.password.isBlank()
            )

            ElevatedButton(
                colors = ButtonDefaults.buttonColors(containerColor = JameoBlue),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .height(48.dp),
                onClick = { viewModel.login(user) }
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
                        checkedIconColor = JameoGreen,
                        uncheckedTrackColor = MaterialTheme.colorScheme.outlineVariant,
                        uncheckedThumbColor = Color.White,
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
