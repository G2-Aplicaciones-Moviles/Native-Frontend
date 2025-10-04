package pe.edu.upc.jameofit.home.presentation.scaffold

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScaffold(
    currentDestination: String,
    onNavigateBottom: (String) -> Unit,
    onNavigateDrawer: (String) -> Unit,
    topTitle: String,
    content: @Composable (Modifier) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "JameoFit",
                    style = MaterialTheme.typography.titleLarge,
                )
                NavigationDrawerItem(
                    label = { Text("Perfil") },
                    selected = currentDestination == "drawer_profile",
                    onClick = { onNavigateDrawer("drawer_profile") }
                )
                NavigationDrawerItem(
                    label = { Text("Ajustes") },
                    selected = currentDestination == "drawer_settings",
                    onClick = { onNavigateDrawer("drawer_settings") }
                )
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
                        icon = { Icon(Icons.Default.Home, null) },
                        label = { Text("Tips") }
                    )
                    NavigationBarItem(
                        selected = currentDestination == "bottom_messages",
                        onClick = { onNavigateBottom("bottom_messages") },
                        icon = { Icon(Icons.Default.Email, null) },
                        label = { Text("Mensajes") }
                    )
                    NavigationBarItem(
                        selected = currentDestination == "bottom_profile",
                        onClick = { onNavigateBottom("bottom_profile") },
                        icon = { Icon(Icons.Default.Person, null) },
                        label = { Text("Perfil") }
                    )
                }
            }
        ) { inner ->
            content(Modifier.padding(inner))
        }
    }
}