package com.example.movieexplorer.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.movieexplorer.R
import com.example.movieexplorer.constants.Constants.DETAIL_SCREEN
import com.example.movieexplorer.constants.Constants.FAVOURITE_SCREEN
import com.example.movieexplorer.constants.Constants.HOME_SCREEN
import com.example.movieexplorer.constants.Constants.IMAGE_SCREEN
import com.example.movieexplorer.constants.Constants.PROFILE_SCREEN
import com.example.movieexplorer.constants.Constants.SEARCH_SCREEN
import com.example.movieexplorer.model.BottomNavItem

@Composable
fun MainScreen(navController: NavHostController, darkTheme: Boolean, function: () -> Unit) {

    val innerNavController = rememberNavController()
    val navBackStackEntry by innerNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface {
        Scaffold(
            topBar = {
                if (currentRoute !in listOf(DETAIL_SCREEN, SEARCH_SCREEN)) {
                    AppTopBar(navController = innerNavController)
                }
            },
            bottomBar = {
                if (currentRoute !in listOf(DETAIL_SCREEN, SEARCH_SCREEN)) {
                    BottomNavigationBar(navController = innerNavController)
                }
            }
        ) { paddingValues ->
            NavHostContainer(navController = innerNavController, padding = paddingValues)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    navController: NavHostController,
) {
    val context = LocalContext.current
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.movie_explorer),
                maxLines = 1,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "",
                modifier = Modifier.clickable {
                    Toast.makeText(context, "Search Screen", Toast.LENGTH_SHORT).show()
                    navController.navigate(SEARCH_SCREEN)
                }
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Red)
    )
}

@Composable
fun NavHostContainer(navController: NavHostController, padding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = HOME_SCREEN,
        modifier = Modifier.padding(padding)
    ) {
        composable(HOME_SCREEN) {
            HomeScreen(navController)
        }
        composable(FAVOURITE_SCREEN) {
            FavouriteScreen(navController)
        }
        composable(PROFILE_SCREEN) {
            ProfileScreen(navController)
        }
        composable(SEARCH_SCREEN) {
            SearchScreen(navController)
        }
        composable("$DETAIL_SCREEN/{imdbId}") { navBackStackEntry ->
            val imdbId = navBackStackEntry.arguments?.getString("imdbId")
            DetailScreen(imdbId!!, navController)
        }
        composable("$IMAGE_SCREEN/{imageUrl}") { navBackStackEntry ->
            val imageUrl = navBackStackEntry.arguments?.getString("imageUrl")
            ImageScreen(imageUrl = imageUrl!!)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val bottomNavItems = listOf(
        BottomNavItem(
            label = stringResource(id = R.string.home),
            icon = Icons.Filled.Home,
            route = HOME_SCREEN
        ),
        BottomNavItem(
            label = stringResource(id = R.string.favourite),
            icon = Icons.Outlined.Favorite,
            route = FAVOURITE_SCREEN
        ),
        BottomNavItem(
            label = stringResource(id = R.string.profile),
            icon = Icons.Outlined.Person,
            route = PROFILE_SCREEN
        )
    )

    BottomAppBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        bottomNavItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(imageVector = navItem.icon, contentDescription = navItem.label) },
                label = { Text(text = navItem.label) },
                alwaysShowLabel = false
            )
        }
    }
}