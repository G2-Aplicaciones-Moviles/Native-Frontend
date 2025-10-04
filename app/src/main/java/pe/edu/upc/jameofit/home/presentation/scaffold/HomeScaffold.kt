package pe.edu.upc.jameofit.home.presentation.scaffold

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pe.edu.upc.jameofit.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScaffold(
    currentDestination: String,
    onNavigateBottom: (String) -> Unit,
    onNavigateDrawer: (String) -> Unit,
    onRequestLogout: () -> Unit,
    topTitle: String,
    content: @Composable (Modifier) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.background,
                drawerContentColor = MaterialTheme.colorScheme.onBackground
            ) {
                DrawerHeader()
                NavigationDrawerItem(
                    label = { Text("Perfil") },
                    selected = currentDestination == "drawer_profile",
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateDrawer("drawer_profile")
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text("Ajustes") },
                    selected = currentDestination == "drawer_settings",
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateDrawer("drawer_settings")
                    },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) }
                )

                Spacer(Modifier.weight(1f))

                FilledTonalButton(
                    onClick = {
                        scope.launch { drawerState.close() }
                        onRequestLogout()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Salir de la cuenta")
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(topTitle) },
                    navigationIcon = {
                        IconButton(onClick = {
                            if (drawerState.isOpen) scope.launch { drawerState.close() }
                            else scope.launch { drawerState.open() }
                        }) { Icon(Icons.Default.Menu, contentDescription = null) }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentDestination == "bottom_tracking",
                        onClick = { onNavigateBottom("bottom_tracking") },
                        icon = { Icon(Icons.Default.Home, null) },
                        label = { Text("Inicio") }
                    )
                    NavigationBarItem(
                        selected = currentDestination == "bottom_tips",
                        onClick = { onNavigateBottom("bottom_tips") },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.lightbulb_24px),
                                contentDescription = null
                            )
                        },
                        label = { Text("Tips") }
                    )
                    NavigationBarItem(
                        selected = currentDestination == "bottom_nutritionists",
                        onClick = { onNavigateBottom("bottom_nutritionists") },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.clinical_notes_24px),
                                null
                            )
                        },
                        label = { Text("Nutricionistas") }
                    )
                    NavigationBarItem(
                        selected = currentDestination == "bottom_messages",
                        onClick = { onNavigateBottom("bottom_messages") },
                        icon = { Icon(Icons.Default.Email, null) },
                        label = { Text("Mensajes") }
                    )
                }
            }
        ) { inner ->
            content(Modifier.padding(inner))
        }
    }
}


@Composable
private fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo JameoFit",
                modifier = Modifier
                    .width(250.dp)
            )
        }
        HorizontalDivider(
            Modifier.padding(top = 16.dp),
            DividerDefaults.Thickness,
            DividerDefaults.color
        )
    }
}