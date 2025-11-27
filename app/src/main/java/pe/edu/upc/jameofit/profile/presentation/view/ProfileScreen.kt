package pe.edu.upc.jameofit.profile.presentation.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.upc.jameofit.R
import pe.edu.upc.jameofit.iam.presentation.viewmodel.AuthViewModel
import pe.edu.upc.jameofit.profile.presentation.viewmodel.ProfileUiState
import pe.edu.upc.jameofit.profile.presentation.viewmodel.ProfileViewModel
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.material3.LocalContentColor

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    authViewModel: AuthViewModel,
    onEditPreferences: () -> Unit,
    onViewMealPlans: () -> Unit,
    onViewTips: () -> Unit,
    onViewDailySummary: () -> Unit,
    onLogout: () -> Unit
) {
    val authUser by authViewModel.user.collectAsState()
    val uiState by profileViewModel.uiState.collectAsState()

    LaunchedEffect(authUser.id) {
        if (authUser.id > 0) {
            profileViewModel.getProfileById(authUser.id)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            // más padding inferior para evitar que el último botón quede "pegado"
            .padding(horizontal = 20.dp, vertical = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(6.dp))

        // Logo libre (sin círculo)
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo JameoFit",
            modifier = Modifier
                .size(110.dp)
                .clip(RoundedCornerShape(8.dp)) // opcional: pequeño radio
        )

        Spacer(Modifier.height(14.dp))

        Text(
            text = authUser.username.ifBlank { "Usuario" },
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(Modifier.height(12.dp))

        when (val state = uiState) {
            is ProfileUiState.Success -> {
                val profile = state.profile

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(18.dp)
                    ) {
                        ProfileInfoRow("Peso", "${profile.weight} kg")
                        Spacer(Modifier.height(6.dp))
                        ProfileInfoRow("Altura", "${profile.height} m")
                        Spacer(Modifier.height(6.dp))
                        ProfileInfoRow("Nivel de actividad", getActivityLevelName(profile.activityLevelId))
                        Spacer(Modifier.height(6.dp))
                        ProfileInfoRow("Objetivo", getObjectiveName(profile.objectiveId))
                    }
                }
            }
            is ProfileUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
            is ProfileUiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {}
        }

        Spacer(Modifier.height(18.dp))

        ProfileActionButton(
            text = "Editar preferencias",
            onClick = onEditPreferences
        )

        ProfileActionButton(
            text = "Resumen del día",
            onClick = onViewDailySummary
        )

        Spacer(Modifier.height(18.dp))

        // Area de tarjetas: cada tarjeta con su propio padding y separación
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickAccessCard(
                title = "Plan de comidas",
                iconRes = R.drawable.menu,
                modifier = Modifier.weight(1f),
                onClick = onViewMealPlans
            )
            QuickAccessCard(
                title = "Tips",
                iconRes = R.drawable.logo2,
                modifier = Modifier.weight(1f),
                onClick = onViewTips
            )
        }

        Spacer(Modifier.height(28.dp))

        OutlinedButton(
            onClick = onLogout,
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = Color(0xFFD32F2F)
            ),
            border = BorderStroke(1.dp, Color(0xFFD32F2F))
        ) {
            Text(
                "Salir de la cuenta",
                color = LocalContentColor.current,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(6.dp))
    }
}

@Composable
private fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ProfileActionButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .padding(vertical = 6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF099FE1),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
    ) {
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun QuickAccessCard(
    title: String,
    iconRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .height(120.dp)
            .padding(2.dp), // espacio propio de la carta
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(44.dp)
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Helper functions
private fun getActivityLevelName(id: Int): String = when (id) {
    1 -> "Sedentario"
    2 -> "Ligeramente Activo"
    3 -> "Moderadamente Activo"
    4 -> "Muy Activo"
    5 -> "Extremadamente Activo"
    else -> "Nivel $id"
}

private fun getObjectiveName(id: Int): String = when (id) {
    1 -> "Bajar de peso"
    2 -> "Mantener peso"
    3 -> "Ganar masa muscular"
    else -> "Objetivo $id"
}
