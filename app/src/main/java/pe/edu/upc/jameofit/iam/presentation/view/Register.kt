package pe.edu.upc.jameofit.iam.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import pe.edu.upc.jameofit.R
import pe.edu.upc.jameofit.iam.presentation.viewmodel.AuthViewModel
import pe.edu.upc.jameofit.shared.presentation.components.ErrorSnackbarHost
import pe.edu.upc.jameofit.shared.presentation.components.showErrorOnce
import pe.edu.upc.jameofit.ui.theme.JameoBlue

@Composable
fun Register(
    viewModel: AuthViewModel,
    goToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val user by viewModel.user.collectAsState()
    val registerSuccess by viewModel.registerSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var passwordVisible by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }

    // Observa registerSuccess
    LaunchedEffect(registerSuccess) {
        when (registerSuccess) {
            true -> {
                isSubmitting = false
                onRegisterSuccess()
                viewModel.resetRegisterSuccess()
            }
            false -> {
                isSubmitting = false
                viewModel.resetRegisterSuccess()
            }
            null -> { /* nothing */ }
        }
    }

    // Errores → Snackbar
    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            scope.launch { snackbarHostState.showErrorOnce(errorMessage!!) }
            viewModel.resetErrorMessage()
            isSubmitting = false
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
                text = "Registrarse",
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
                placeholder = { Text(text = "Mínimo 6 caracteres") },
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
                            painter = painterResource(
                                id = if (passwordVisible) R.drawable.visibility_24px else R.drawable.visibility_off_24px
                            ),
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
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF099FE1)),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .height(48.dp),
                onClick = {
                    isSubmitting = true
                    viewModel.register(user) // <-- PASAMOS user al ViewModel
                },
                enabled = !isSubmitting
            ) {
                Text(
                    text = if (isSubmitting) "Creando cuenta..." else "Crear Cuenta",
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
                    .padding(top = 16.dp)
                    .padding(horizontal = 20.dp)
            )

            Spacer(Modifier.height(24.dp))

            Row {
                Text("¿Ya tienes una cuenta? ")
                Text(
                    text = "Inicia sesión",
                    color = JameoBlue,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { goToLogin() }
                )
            }
        }

        ErrorSnackbarHost(hostState = snackbarHostState)
    }
}
