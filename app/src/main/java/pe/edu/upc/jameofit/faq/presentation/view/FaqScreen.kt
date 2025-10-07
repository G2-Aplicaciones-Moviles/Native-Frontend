package pe.edu.upc.jameofit.faq.presentation.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import pe.edu.upc.jameofit.ui.theme.JameoBlue
import pe.edu.upc.jameofit.ui.theme.JameoGreen

enum class FaqAudience { USER, NUTRITIONIST }

data class FaqItem(
    val question: String,
    val answer: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaqScreen(
    modifier: Modifier = Modifier,
    initialAudience: FaqAudience = FaqAudience.USER,
    onBack: (() -> Unit)? = null
) {
    // Fallbacks por si no estuvieran importadas tus variables de tema
    val primary = runCatching { JameoBlue }.getOrNull() ?: Color(0xFF077CB0)
    val accent  = runCatching { JameoGreen }.getOrNull() ?: Color(0xFF0C893A)

    var selectedTab by rememberSaveable { mutableStateOf(initialAudience) }
    var query by rememberSaveable { mutableStateOf("") }

    val fullList = remember(selectedTab) {
        if (selectedTab == FaqAudience.USER) userFaq() else nutritionistFaq()
    }
    val filtered = remember(query, fullList) {
        if (query.isBlank()) fullList
        else fullList.filter {
            it.question.contains(query, ignoreCase = true) ||
                    it.answer.contains(query, ignoreCase = true)
        }
    }

    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Text("Preguntas frecuentes", fontWeight = FontWeight.SemiBold)
                },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Volver", tint = Color.Black)
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Tabs: Usuario / Nutricionista
            TabRow(
                selectedTabIndex = if (selectedTab == FaqAudience.USER) 0 else 1,
                containerColor = Color.Transparent,
                contentColor = Color.Gray,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[if (selectedTab == FaqAudience.USER) 0 else 1]),
                        color = JameoGreen
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == FaqAudience.USER,
                    onClick = { selectedTab = FaqAudience.USER },
                    text = { Text("Usuario") },
                    selectedContentColor = JameoBlue
                )
                Tab(
                    selected = selectedTab == FaqAudience.NUTRITIONIST,
                    onClick = { selectedTab = FaqAudience.NUTRITIONIST },
                    text = { Text("Nutricionista") },
                    selectedContentColor = JameoBlue
                )
            }

            Spacer(Modifier.height(12.dp))

            // Buscador
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar en preguntas…") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray
                )
            )

            Spacer(Modifier.height(12.dp))

            if (filtered.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No se encontró nada con '$query'.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filtered) { item ->
                        ExpandableFaqCard(
                            item = item,
                            highlightColor = accent
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpandableFaqCard(
    item: FaqItem,
    highlightColor: Color
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F0F0)
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.question,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = highlightColor
                )
            }
            if (expanded) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = item.answer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

/* -------------------- CONTENIDO (adáptalo a tu realidad) -------------------- */

private fun userFaq(): List<FaqItem> = listOf(
    FaqItem(
        "¿Cómo activo o desactivo el seguimiento (tracking)?",
        "Desde Inicio, usa el control 'Activar/Desactivar seguimiento'. Si está activado, tu resumen del día y el progreso semanal se actualizan automáticamente."
    ),
    FaqItem(
        "¿Cómo registro mis comidas del día?",
        "En 'Comidas del día' toca 'Agregar comida' y completa los campos. Tus métricas (número de comidas, calorías y porcentaje de progreso) se recalculan en el resumen."
    ),
    FaqItem(
        "¿Dónde configuro mis objetivos de calorías y ritmo?",
        "Ve a Gestión de Objetivos → define objetivo (perder/ganar/mantener), peso objetivo, ritmo y tipo de dieta. Luego guarda la configuración."
    ),
    FaqItem(
        "¿Qué muestra el 'Progreso semanal'?",
        "Resumen de cumplimiento por días (por ejemplo 4/4 comidas registradas) y tu avance respecto a las metas fijadas para la semana."
    ),
    FaqItem(
        "¿Cómo contacto a un nutricionista?",
        "Desde la pestaña 'Nutricionistas' puedes ver perfiles, elegir uno y enviar una solicitud o mensaje. Si el profesional acepta, se habilita el seguimiento."
    )
)

private fun nutritionistFaq(): List<FaqItem> = listOf(
    FaqItem(
        "¿Cómo creo o edito planes alimenticios para mis usuarios?",
        "Ingresa al perfil del usuario → Plan alimenticio → Crear/Editar. Puedes partir de plantillas y personalizar por objetivos, alergias y preferencias."
    ),
    FaqItem(
        "¿Puedo monitorear adherencia y métricas en tiempo real?",
        "Sí. Visualiza registro de comidas, cumplimiento diario y KPIs básicos (adherencia, calorías objetivo vs. real) en el panel del usuario."
    ),
    FaqItem(
        "¿Cómo comparto contenido educativo?",
        "Desde tu panel de profesional, crea publicaciones o tips. Se entregan a tus usuarios y pueden aparecer en la sección 'Tips' del inicio."
    ),
    FaqItem(
        "¿La plataforma permite monetizar mis servicios?",
        "La modalidad prevista contempla suscripciones y/o consultas privadas gestionadas desde tu panel. Ajusta precios, disponibilidad y condiciones."
    ),
    FaqItem(
        "¿Qué medidas hay para la privacidad de los pacientes?",
        "Solo ves datos de los usuarios que te han autorizado. La información se maneja con buenas prácticas de seguridad y se puede revocar el acceso."
    )
)