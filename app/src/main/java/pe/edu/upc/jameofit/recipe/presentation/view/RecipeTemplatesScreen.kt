@file:OptIn(ExperimentalMaterial3Api::class)

package pe.edu.upc.jameofit.recipe.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pe.edu.upc.jameofit.R
import pe.edu.upc.jameofit.recipe.data.model.RecipeTemplateResponse
import pe.edu.upc.jameofit.recipe.presentation.viewmodel.RecipeViewModel
import pe.edu.upc.jameofit.ui.theme.JameoBlue
import pe.edu.upc.jameofit.ui.theme.JameoGreen

@Composable
fun RecipeTemplatesScreen(
    navController: NavHostController,
    viewModel: RecipeViewModel,
    currentNutritionistId: Long,
    modifier: Modifier = Modifier
) {
    val templates by viewModel.detailedTemplates.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val filterId by viewModel.filterNutritionistId.collectAsState()
    val allNutritionists by viewModel.allNutritionists.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAllNutritionists()
    }

    LaunchedEffect(filterId, allNutritionists.size) {
        if (allNutritionists.isNotEmpty() || filterId == 0L || filterId == null) {
            viewModel.loadTemplatesWithAuthor()
        }
    }

    val filterOptions = remember(allNutritionists, currentNutritionistId) {
        val options = mutableListOf<Pair<Long, String>>()

        options.add(0L to "Todos los Expertos")

        val loggedNutri = allNutritionists.firstOrNull { it.userId == currentNutritionistId }
        val loggedNutriName = loggedNutri?.name ?: "Mi Perfil"

        if (loggedNutri != null) {
            options.add(currentNutritionistId to "Mis Recetas ($loggedNutriName)")
        }

        allNutritionists.forEach { nutri ->
            if (nutri.userId != currentNutritionistId) {
                val safeName = nutri.name ?: "Autor Desconocido"
                options.add(nutri.userId to safeName)
            }
        }

        return@remember options.toList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Plantillas de Recetas", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = JameoGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            when {
                isLoading && templates.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = JameoGreen
                    )
                }
                error != null -> {
                    Text(
                        text = "Error al cargar templates: $error",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center).padding(32.dp)
                    )
                }
                templates.isEmpty() && !isLoading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center).padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No hay plantillas de recetas disponibles.", color = Color.Gray)
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        item {
                            // Selector de Filtro
                            FilterSelector(
                                currentFilterId = filterId ?: 0L,
                                options = filterOptions,
                                onFilterSelected = { id ->
                                    viewModel.setFilterNutritionistId(id)
                                },
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        items(templates) { template ->
                            RecipeTemplateCard(
                                template = template,
                                onClick = {
                                    // Navegar al detalle de la receta (en modo solo vista, 0L)
                                    navController.navigate("drawer/recipe_detail/0/${template.id}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ------------------------------------------------------------
// Componentes Reutilizables (FilterSelector y RecipeTemplateCard)
// ------------------------------------------------------------

@Composable
fun FilterSelector(
    currentFilterId: Long,
    options: List<Pair<Long, String>>,
    onFilterSelected: (Long) -> Unit,
    modifier: Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedLabel = options.firstOrNull { it.first == currentFilterId }?.second ?: "Todos los Expertos"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedLabel,
            onValueChange = {},
            readOnly = true,
            label = { Text("Filtrar por Autor") },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = JameoBlue,
                unfocusedIndicatorColor = Color.LightGray
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { (id, label) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        onFilterSelected(id)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun RecipeTemplateCard(
    template: RecipeTemplateResponse,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ... (Ícono y Box se mantienen) ...

            Spacer(modifier = Modifier.width(16.dp))

            // Información
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // 1. Nombre de la Receta (Protegido)
                Text(
                    text = template.name ?: "Receta sin nombre",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = JameoBlue
                )

                // 2. Descripción (Protegido)
                Text(
                    text = template.description ?: "Sin descripción.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.info_nutri),
                        contentDescription = "Nutricionista",
                        modifier = Modifier.size(16.dp),
                        tint = JameoBlue
                    )
                    // 3. Nombre del Autor (Protegido)
                    Text(
                        text = template.nutritionistName ?: "Autor Desconocido",
                        style = MaterialTheme.typography.labelMedium,
                        color = JameoBlue,
                        fontWeight = FontWeight.SemiBold
                    )

                    Surface(
                        color = JameoGreen.copy(alpha = 0.1f),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = template.category ?: "Sin Categoría",
                            style = MaterialTheme.typography.labelSmall,
                            color = JameoGreen,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}