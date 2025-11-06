package pe.edu.upc.jameofit.profile.presentation.view

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetup(
    authUserId: Long,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val pref = remember { context.getSharedPreferences("pref_profile", Context.MODE_PRIVATE) }

    var txtNombre by remember { mutableStateOf("") }
    var txtPeso by remember { mutableStateOf("") }
    var txtAltura by remember { mutableStateOf("") }
    var txtEdad by remember { mutableStateOf("") } // podemos eliminar si usamos año
    var txtGenero by remember { mutableStateOf("") }

    // Dropdown Género
    val generos = listOf("MALE", "FEMALE", "OTHER")
    var expandedGenero by remember { mutableStateOf(false) }

    // Dropdown Año de nacimiento
    val currentYear = java.time.LocalDate.now().year
    val years = (currentYear downTo currentYear - 100).toList() // últimos 100 años
    var selectedYear by remember { mutableStateOf<Int?>(null) }
    var expandedYear by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
        ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Completa tu perfil",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Nombre
            OutlinedTextField(
                value = txtNombre,
                onValueChange = { txtNombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Peso
            OutlinedTextField(
                value = txtPeso,
                onValueChange = { txtPeso = it.filter { c -> c.isDigit() || c == '.' } },
                label = { Text("Peso (kg)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Altura
            OutlinedTextField(
                value = txtAltura,
                onValueChange = { txtAltura = it.filter { c -> c.isDigit() || c == '.' } },
                label = { Text("Altura (m)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Dropdown Género
            ExposedDropdownMenuBox(
                expanded = expandedGenero,
                onExpandedChange = { expandedGenero = !expandedGenero }
            ) {
                OutlinedTextField(
                    value = txtGenero,
                    onValueChange = {},
                    label = { Text("Género") },
                    readOnly = true,
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedGenero,
                    onDismissRequest = { expandedGenero = false }
                ) {
                    generos.forEach { genero ->
                        DropdownMenuItem(
                            text = { Text(genero) },
                            onClick = {
                                txtGenero = genero
                                expandedGenero = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Dropdown Año de nacimiento
            ExposedDropdownMenuBox(
                expanded = expandedYear,
                onExpandedChange = { expandedYear = !expandedYear }
            ) {
                OutlinedTextField(
                    value = selectedYear?.toString() ?: "",
                    onValueChange = {},
                    label = { Text("Año de nacimiento") },
                    placeholder = { Text("Selecciona año") },
                    readOnly = true,
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedYear,
                    onDismissRequest = { expandedYear = false }
                ) {
                    years.forEach { year ->
                        DropdownMenuItem(
                            text = { Text(year.toString()) },
                            onClick = {
                                selectedYear = year
                                expandedYear = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            ElevatedButton(
                onClick = {
                    if (txtNombre.isBlank() || txtPeso.isBlank() || txtAltura.isBlank() || txtGenero.isBlank() || selectedYear == null) {
                        Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                        return@ElevatedButton
                    }

                    // Guardar en SharedPreferences
                    pref.edit {
                        putString("nombre", txtNombre)
                        putString("peso", txtPeso)
                        putString("height", txtAltura)
                        putString("gender", txtGenero)
                        putString("birthDate", "${selectedYear}-01-01")
                    }

                    onNext()
                },
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("Continuar")
            }
        }
    }
}
