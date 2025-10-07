package pe.edu.upc.jameofit.tracking.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.upc.jameofit.R
import pe.edu.upc.jameofit.ui.theme.JameoBlue
import pe.edu.upc.jameofit.ui.theme.JameoGreen

@Composable
fun TrackingHomeScreen(
    onOpenRecentActivity: () -> Unit,
    onOpenWeeklyProgress: () -> Unit,
    onOpenTips: () -> Unit,
) {
    // Estado del tracking (solo UI por ahora)
    var trackingEnabled by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de la aplicación",
                modifier = Modifier
                    .size(70.dp)
                    .padding(bottom = 2.dp)
            )
        }
        item {
            Spacer(Modifier.height(2.dp))
        }
        item {
            Text(
                text = "Sebastian Rodriguez",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
            )
        }
        item {
            Spacer(Modifier.height(16.dp))
        }

        // Control de Tracking
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (trackingEnabled) Color(0xFFF5F5F5) else Color(0xFFFFEBEE)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Tracking de Comidas",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = if (trackingEnabled) "Activo" else "Desactivado",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (trackingEnabled) JameoGreen else MaterialTheme.colorScheme.error
                        )
                    }
                    Switch(
                        checked = trackingEnabled,
                        onCheckedChange = { trackingEnabled = it },
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
                        thumbContent = if (trackingEnabled) {
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
                }
            }
        }

        item {
            Spacer(Modifier.height(16.dp))
        }

        // Resumen del día
        item {
            Text("Resumen del día", style = MaterialTheme.typography.titleMedium)
        }
        item {
            Spacer(Modifier.height(8.dp))
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(title = "Comidas", value = "0")
                StatCard(title = "Progreso", value = "0%")
                StatCard(title = "Tips saludables", value = "0")
            }
        }
        item {
            Spacer(Modifier.height(16.dp))
        }

        item {
            SectionHeader(title = "Actividad reciente", onClick = onOpenRecentActivity)
        }
        item {
            ActivityRowItem(iconRes = android.R.drawable.ic_menu_recent_history, title = "Almuerzo", time = "Hace 1 hora")
        }
        item {
            ActivityRowItem(iconRes = android.R.drawable.ic_menu_recent_history, title = "Desayuno", time = "Hace 5 horas")
        }

        item {
            Spacer(Modifier.height(16.dp))
        }
        item {
            SectionHeader(title = "Progreso Semanal", onClick = onOpenWeeklyProgress)
        }
        item {
            WeekItem(day = "Lunes", desc = "4/4 meals completed")
        }
        item {
            WeekItem(day = "Martes", desc = "4/4 meals completed")
        }
        item {
            WeekItem(day = "Miércoles", desc = "4/4 meals completed")
        }
        item {
            WeekItem(day = "Jueves", desc = "0/4 meals completed")
        }
        item {
            WeekItem(day = "Viernes", desc = "0/4 meals completed")
        }

        item {
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable private fun StatCard(title: String, value: String) {
    Column(
        modifier = Modifier
            .border(1.dp,Color.LightGray, RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Text(title, style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(4.dp))
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = JameoGreen)
    }
}


@Composable private fun SectionHeader(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        TextButton(onClick = onClick) { Text("Ver todo") }
    }
}


@Composable private fun ActivityRowItem(iconRes: Int, title: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = iconRes), contentDescription = null)
            Spacer(Modifier.width(12.dp))
            Text(title)
        }
        Text(time, style = MaterialTheme.typography.bodySmall, color = JameoGreen)
    }
    Divider(thickness = 0.5.dp)
}


@Composable private fun WeekItem(day: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(24.dp).background(Color.LightGray, RoundedCornerShape(4.dp)))
            Spacer(Modifier.width(12.dp))
            Column { Text(day); Text(desc, style = MaterialTheme.typography.bodySmall, color = JameoGreen) }
        }
    }
    Divider(thickness = 0.5.dp)
}