package pe.edu.upc.jameofit.tracking.presentation.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import pe.edu.upc.jameofit.iam.presentation.viewmodel.AuthViewModel
import pe.edu.upc.jameofit.ui.theme.JameoGreen
import pe.edu.upc.jameofit.tracking.presentation.viewmodel.TrackingViewModel

@Composable
fun TrackingHomeScreen(
    viewModel: TrackingViewModel,
    authViewModel: AuthViewModel,
    userId: Long,
    onOpenRecentActivity: () -> Unit,
    onOpenWeeklyProgress: () -> Unit,
    onOpenTips: () -> Unit,
) {
    val tracking by viewModel.tracking.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val username by authViewModel.user.collectAsState()

    // ✅ Cargar datos al montar la pantalla
    LaunchedEffect(userId) {
        if (userId > 0) {
            viewModel.loadTrackingByUserId(userId)
            viewModel.loadProgress(userId)
        }
    }

    val consumedCalories = progress?.consumed?.calories ?: 0.0
    val targetCalories = progress?.target?.calories ?: 0.0
    val consumedCarbs = progress?.consumed?.carbs ?: 0.0
    val targetCarbs = progress?.target?.carbs ?: 0.0
    val consumedProteins = progress?.consumed?.proteins ?: 0.0
    val targetProteins = progress?.target?.proteins ?: 0.0
    val consumedFats = progress?.consumed?.fats ?: 0.0
    val targetFats = progress?.target?.fats ?: 0.0

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text(
                text = "Bienvenido ${username.username.ifBlank { "Usuario" }}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }

        item {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Progreso diario", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))

                        if (targetCalories > 0) {
                            val percent = ((consumedCalories / targetCalories) * 100.0).coerceIn(0.0, 100.0)

                            // Calorías
                            Text(
                                "Calorías: ${consumedCalories.roundToInt()} / ${targetCalories.roundToInt()} kcal",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { (percent / 100f).toFloat() },
                                modifier = Modifier.fillMaxWidth().height(8.dp),
                            )
                            Spacer(Modifier.height(4.dp))
                            Text("${percent.roundToInt()}%", fontSize = 14.sp, color = Color.Gray)

                            Spacer(Modifier.height(16.dp))

                            // Macros
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                MacroItem("Carbos", consumedCarbs, targetCarbs)
                                MacroItem("Proteínas", consumedProteins, targetProteins)
                                MacroItem("Grasas", consumedFats, targetFats)
                            }
                        } else {
                            Text(
                                "Aún no tienes objetivos configurados",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(16.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Comidas",
                    value = tracking?.mealPlanEntries?.size?.toString() ?: "0",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Progreso",
                    value = if (targetCalories > 0) {
                        val percent = ((consumedCalories / targetCalories) * 100.0).coerceIn(0.0, 100.0)
                        "${percent.roundToInt()}%"
                    } else "—",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Tips",
                    value = "0",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MacroItem(name: String, consumed: Double, target: Double) {
    Column {
        Text(name, fontSize = 12.sp, color = Color.Gray)
        Spacer(Modifier.height(4.dp))
        Text(
            "${consumed.roundToInt()}g / ${target.roundToInt()}g",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Text(
                value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = JameoGreen
            )
        }
    }
}