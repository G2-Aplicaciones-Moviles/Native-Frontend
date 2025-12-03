package pe.edu.upc.jameofit.tracking.presentation.view

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import pe.edu.upc.jameofit.iam.presentation.viewmodel.AuthViewModel
import pe.edu.upc.jameofit.mealplan.data.model.MealPlanEntryResponse
import pe.edu.upc.jameofit.tracking.presentation.utils.getMealTypeName
import pe.edu.upc.jameofit.ui.theme.JameoGreen
import pe.edu.upc.jameofit.tracking.presentation.viewmodel.TrackingViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
    val recentMeals by viewModel.recentMeals.collectAsState()
    val hasMealPlan by viewModel.hasMealPlan.collectAsState()

    LaunchedEffect(userId) {
        if (userId > 0) {
            viewModel.loadTrackingByUserId(userId)
            viewModel.loadProgress(userId)
            viewModel.loadRecentActivity(userId)
        }
    }

    val consumedCalories = progress?.consumed?.calories ?: 0.0
    val targetCalories = progress?.target?.calories ?: 0.0
    val percentCalories =
        if (targetCalories > 0) {
            ((consumedCalories / targetCalories) * 100).coerceIn(0.0, 100.0)
        } else 0.0

    val animatedProgress by animateFloatAsState(
        targetValue = (percentCalories / 100f).toFloat().coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
    )


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F7F5))
    ) {

        // --------------------------------------------------------
        // HEADER MEGA ULTRA PREMIUM 💎
        // --------------------------------------------------------
        item {
            // HEADER MEGA ULTRA PREMIUM – SIN AVATAR
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                JameoGreen,
                                JameoGreen.copy(alpha = 0.8f),
                                JameoGreen.copy(alpha = 0.6f),
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Hola, ${username.username}",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "Revisa tu actividad del día",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

        }

        // --------------------------------------------------------
        // DAILY PROGRESS CARD (GLASS STYLE)
        // --------------------------------------------------------
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = (-20).dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.85f)
                ),
                elevation = CardDefaults.cardElevation(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                ) {

                    Text(
                        "Progreso diario",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = JameoGreen
                    )
                    Spacer(Modifier.height(16.dp))

                    if (targetCalories > 0) {

                        Text(
                            "Calorías",
                            fontWeight = FontWeight.Medium,
                            color = Color.DarkGray
                        )
                        Spacer(Modifier.height(6.dp))

                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(14.dp)
                                .clip(RoundedCornerShape(50)),
                            color = JameoGreen,
                            trackColor = Color.LightGray.copy(alpha = 0.2f)
                        )

                        Spacer(Modifier.height(8.dp))
                        Text(
                            "${consumedCalories.toInt()} / ${targetCalories.toInt()} kcal",
                            color = Color.Gray
                        )

                        Spacer(Modifier.height(20.dp))

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MacroChipPremium(
                                "Carbos",
                                consumedCalories = progress?.consumed?.carbs,
                                targetCalories = progress?.target?.carbs,
                            )
                            MacroChipPremium(
                                "Proteínas",
                                consumedCalories = progress?.consumed?.proteins,
                                targetCalories = progress?.target?.proteins,
                            )
                            MacroChipPremium(
                                "Grasas",
                                consumedCalories = progress?.consumed?.fats,
                                targetCalories = progress?.target?.fats,
                            )
                        }

                    } else {
                        Text(
                            "Aún no tienes objetivos configurados",
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        // --------------------------------------------------------
        // QUICK STATS (NEW STYLE)
        // --------------------------------------------------------
        item {
            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FancyStatCardV2(
                    title = "Comidas",
                    value = tracking?.mealPlanEntries?.size?.toString() ?: "0",
                )
                Spacer(modifier = Modifier.width(16.dp))
                FancyStatCardV2(
                    title = "Progreso",
                    value = "${percentCalories.toInt()}%",
                )
            }
        }


        // --------------------------------------------------------
        // RECENT ACTIVITY SECTION
        // --------------------------------------------------------
        item {
            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Actividad Reciente",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                if (hasMealPlan && recentMeals.isNotEmpty()) {
                    TextButton(onClick = onOpenRecentActivity) {
                        Text("Ver todo", color = JameoGreen)
                    }
                }
            }
        }

        // RECENT MEALS OR EMPTY STATES
        when {
            !hasMealPlan -> item {
                EmptyPremiumCard(
                    title = "Aún no tienes un meal plan",
                    subtitle = "Crea uno para comenzar tu progreso."
                )
            }

            recentMeals.isEmpty() -> item {
                EmptyPremiumCard(
                    title = "No hay comidas registradas",
                    subtitle = "Añade una receta para empezar."
                )
            }

            else -> items(recentMeals) { meal ->
                TimelineMealCard(meal)
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}



@Composable
fun MacroChipPremium(
    name: String,
    consumedCalories: Double?,
    targetCalories: Double?,
) {
    val c = consumedCalories ?: 0.0
    val t = targetCalories ?: 0.0

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.6f))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(name, fontSize = 12.sp, color = Color.Gray)
        Text(
            "${c.toInt()} / ${t.toInt()}",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = JameoGreen
        )
    }
}


@Composable
fun FancyStatCardV2(title: String, value: String) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(title, color = Color.Gray, fontSize = 13.sp)
            Spacer(Modifier.height(6.dp))
            Text(
                value,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = JameoGreen
            )
        }
    }
}


@Composable
fun TimelineMealCard(meal: MealPlanEntryResponse) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(CircleShape)
                .background(JameoGreen)
        )

        Spacer(Modifier.width(14.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    meal.recipeName ?: "Comida sin nombre",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
                Text(
                    getMealTypeName(meal.mealPlanType),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun EmptyPremiumCard(title: String, subtitle: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 14.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.8f)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(subtitle, fontSize = 14.sp, color = Color.Gray)
        }
    }
}



