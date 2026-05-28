package be.kdg.filmapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import be.kdg.filmapp.R
import be.kdg.filmapp.ui.screens.DirectorDetailScreen
import be.kdg.filmapp.ui.screens.DirectorListScreen
import be.kdg.filmapp.ui.screens.SettingsScreen

const val DIRECTOR_LIST_ROUTE = "director_list"
const val DIRECTOR_DETAIL_ROUTE = "director_detail/{directorId}"
const val SETTINGS_ROUTE = "settings"

fun directorDetailRoute(id: Int) = "director_detail/$id"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    val currentEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentEntry?.destination?.route
    val canNavigateBack = navController.previousBackStackEntry != null

    val title = when {
        currentRoute == DIRECTOR_LIST_ROUTE -> stringResource(R.string.screen_director_list)
        currentRoute?.startsWith("director_detail") == true -> stringResource(R.string.screen_director_detail)
        currentRoute == SETTINGS_ROUTE -> stringResource(R.string.screen_settings)
        else -> stringResource(R.string.app_name)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    if (canNavigateBack) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                        }
                    }
                },
                actions = {
                    if (currentRoute == DIRECTOR_LIST_ROUTE) {
                        IconButton(onClick = { navController.navigate(SETTINGS_ROUTE) }) {
                            Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.screen_settings))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DIRECTOR_LIST_ROUTE,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(DIRECTOR_LIST_ROUTE) {
                DirectorListScreen(onDirectorClick = { id -> navController.navigate(directorDetailRoute(id)) })
            }
            composable(
                route = DIRECTOR_DETAIL_ROUTE,
                arguments = listOf(navArgument("directorId") { type = NavType.IntType })
            ) {
                DirectorDetailScreen()
            }
            composable(SETTINGS_ROUTE) {
                SettingsScreen()
            }
        }
    }
}
