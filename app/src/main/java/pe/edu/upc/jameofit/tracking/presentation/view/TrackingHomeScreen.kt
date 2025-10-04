package pe.edu.upc.jameofit.tracking.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TrackingHomeScreen(
    onOpenRecentActivity: () -> Unit,
    onOpenWeeklyProgress: () -> Unit,
    onOpenTips: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("JameoFit", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Sebastian Rodriguez",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(Modifier.height(16.dp))


// Resumen del día
        Text("Resumen del día", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(title = "Comidas", value = "0")
            StatCard(title = "Progreso", value = "0%")
            StatCard(title = "Tips saludables", value = "0")
        }
        Spacer(Modifier.height(12.dp))


// buttons (disabled/enabled tracking – only UI)
        OutlinedButton(onClick = { /* no-op */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Desabilitar tracking")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { /* no-op */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Habilitar tracking")
        }


        Spacer(Modifier.height(16.dp))
        SectionHeader(title = "Actividad reciente", onClick = onOpenRecentActivity)
        ActivityRowItem(iconRes = android.R.drawable.ic_menu_recent_history, title = "Almuerzo", time = "Hace 1 hora")
        ActivityRowItem(iconRes = android.R.drawable.ic_menu_recent_history, title = "Desayuno", time = "Hace 5 horas")


        Spacer(Modifier.height(16.dp))
        SectionHeader(title = "Progreso Semanal", onClick = onOpenWeeklyProgress)
        WeekItem(day = "Lunes", desc = "4/4 meals completed")
        WeekItem(day = "Martes", desc = "4/4 meals completed")
        WeekItem(day = "Miércoles", desc = "4/4 meals completed")
        WeekItem(day = "Jueves", desc = "0/4 meals completed")
        WeekItem(day = "Viernes", desc = "0/4 meals completed")


        Spacer(Modifier.weight(1f))
// Bottom bar already provided by HomeScaffold
    }
}

@Composable private fun StatCard(title: String, value: String) {
    Column(
        modifier = Modifier
            .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Text(title, style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(4.dp))
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
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
        Text(time, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
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
            Column { Text(day); Text(desc, style = MaterialTheme.typography.bodySmall, color = Color.Gray) }
        }
    }
    Divider(thickness = 0.5.dp)
}