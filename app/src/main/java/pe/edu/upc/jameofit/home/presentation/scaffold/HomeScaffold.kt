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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pe.edu.upc.jameofit.R
import pe.edu.upc.jameofit.home.presentation.navigation.DrawerRoute
import pe.edu.upc.jameofit.home.presentation.navigation.TabGraph
import pe.edu.upc.jameofit.ui.theme.JameoBlue

/**
 * Root scaffold for the authenticated area:
 * - Hosts the app-level [TopAppBar], the navigation drawer, and the bottom navigation bar.
 * - Delegates content rendering to [content], which receives the inner padding modifier.
 *
 * The component is navigation-agnostic: it only emits callbacks and highlights the selected item
 * based on [currentDestination].
 *
 * @param currentDestination The current navigation "key" used for selection state.
 * Use one of [TabGraph] values for tabs, or one of [DrawerRoute] values for drawer destinations.
 * @param onNavigateBottom Invoked when a bottom tab is clicked with the selected [TabGraph] route.
 * @param onNavigateDrawer Invoked when a drawer item is clicked with the selected [DrawerRoute] route.
 * @param onRequestLogout Invoked when the "Logout" action is pressed in the drawer.
 * @param topTitle Title text displayed in the [TopAppBar].
 * @param content Screen content. Receives a [Modifier] with the scaffold's inner padding.
 */
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

    /**
     * Closes the drawer before running the provided navigation action.
     */
    fun closeAnd(go: () -> Unit) = scope.launch {
        drawerState.close()
        go()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.background,
                drawerContentColor = MaterialTheme.colorScheme.onBackground
            ) {
                DrawerHeader()

                NavigationDrawerItem(
                    label = { Text("Ver Perfil") },
                    selected = currentDestination == DrawerRoute.PROFILE,
                    onClick = { closeAnd { onNavigateDrawer(DrawerRoute.PROFILE) } },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text("Gestionar Objetivos") },
                    selected = currentDestination == DrawerRoute.GOALS,
                    onClick = { closeAnd { onNavigateDrawer(DrawerRoute.GOALS) } },
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.goals),
                            contentDescription = null
                        )
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Analíticas  y Estadísticas") },
                    selected = currentDestination == DrawerRoute.PROGRESS,
                    onClick = { closeAnd { onNavigateDrawer(DrawerRoute.PROGRESS) } },
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.analytics),
                            contentDescription = null
                        )
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Mis Recomendaciones") },
                    selected = currentDestination == DrawerRoute.RECOMMENDATIONS,
                    onClick = { closeAnd { onNavigateDrawer(DrawerRoute.RECOMMENDATIONS) } },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = null) }

                )

                NavigationDrawerItem(
                    label = { Text("Ver Planes Alimenticios") },
                    selected = currentDestination == DrawerRoute.MEAL_PLANS,
                    onClick = { closeAnd { onNavigateDrawer(DrawerRoute.MEAL_PLANS) } },
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.meal_plans),
                            contentDescription = null
                        )
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Suscripciones") },
                    selected = currentDestination == DrawerRoute.SUBSCRIPTIONS,
                    onClick = { closeAnd { onNavigateDrawer(DrawerRoute.SUBSCRIPTIONS) } },
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.subscriptions),
                            contentDescription = null
                        )
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Preguntas Frecuentes") },
                    selected = currentDestination == DrawerRoute.FAQ,
                    onClick = { closeAnd { onNavigateDrawer(DrawerRoute.FAQ) } },
                    icon = { Icon(painterResource(id = R.drawable.faq), contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text("Ajustes") },
                    selected = currentDestination == DrawerRoute.SETTINGS,
                    onClick = { closeAnd { onNavigateDrawer(DrawerRoute.SETTINGS) } },
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.settings),
                            contentDescription = null
                        )
                    }
                )

                Spacer(Modifier.weight(1f))

                Button(
                    onClick = { closeAnd(onRequestLogout) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = JameoBlue,
                        contentColor = Color.White
                    )
                ) { Text("Cerrar Sesión") }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(topTitle) },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if (drawerState.isOpen) drawerState.close() else drawerState.open()
                                }
                            }
                        ) { Icon(Icons.Default.Menu, contentDescription = null) }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentDestination == TabGraph.TRACKING,
                        onClick = { onNavigateBottom(TabGraph.TRACKING) },
                        icon = { Icon(Icons.Default.Home, null) },
                        label = { Text("Inicio") }
                    )
                    NavigationBarItem(
                        selected = currentDestination == TabGraph.TIPS,
                        onClick = { onNavigateBottom(TabGraph.TIPS) },
                        icon = {
                            Icon(
                                painterResource(id = R.drawable.tips),
                                contentDescription = null
                            )
                        },
                        label = { Text("Tips") }
                    )
                    NavigationBarItem(
                        selected = currentDestination == TabGraph.NUTRITIONISTS,
                        onClick = { onNavigateBottom(TabGraph.NUTRITIONISTS) },
                        icon = { Icon(painterResource(id = R.drawable.nutritionists), null) },
                        label = { Text("Nutricionistas") }
                    )
                    NavigationBarItem(
                        selected = currentDestination == TabGraph.MESSAGES,
                        onClick = { onNavigateBottom(TabGraph.MESSAGES) },
                        icon = { Icon(Icons.Default.Email, null) },
                        label = { Text("Mensajes") }
                    )
                }
            }
        ) { inner -> content(Modifier.padding(inner)) }
    }
}

/**
 * Drawer header showing the app logo and a divider.
 */
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
                modifier = Modifier.width(250.dp)
            )
        }
        HorizontalDivider(
            Modifier.padding(top = 16.dp),
            DividerDefaults.Thickness,
            DividerDefaults.color
        )
    }
}