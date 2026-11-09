package com.example.fincare.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fincare.ui.components.FincareBottomBar
import com.example.fincare.ui.components.bottomBarItems
import com.example.fincare.ui.screen.thread.test.ContactExploreScreen
import androidx.compose.foundation.layout.Column
import com.example.fincare.ui.theme.FincareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            FincareApp(navController) {
                NavHost(
                    navController = navController,
                    startDestination = "signInScreen"
                ) {
                    composable("signInScreen") {
                        SignInScreen(navController)
                    }

                    composable("signUpScreen") {
                        SignUpScreen(navController)
                    }

                    composable("homeScreen") {
                        HomeScreen(navController)
                    }

                    composable("investmentsScreen") {
                        InvestmentsScreen(navController)
                    }

                    composable("accountsScreen") {
                        AccountsScreen(navController)
                    }

                    composable("accountFormScreen") {
                        AccountFormScreen(navController)
                    }

                    composable("profileScreen") {
                        ComingSoonScreen("Perfil")
                    }

                    composable("transactionExploreScreen") {
                        TransactionExploreScreen(navController)
                    }

                    composable("transactionFormScreen") {
                        TransactionFormScreen(navController)
                    }

                    composable("exploreScreen") {
//                        ExploreScreen(navController)
                        ContactExploreScreen(navController)
                    }

                    composable("detailScreen")  {
//                        DetailScreen()
                            ContactFormScreen(navController)
                    }
                }
            }
        }
    }

}

fun NavController.navigateTab(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun FincareApp(
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = bottomBarItems.any { it.route == currentRoute }

    FincareTheme() {
        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    FincareBottomBar(
                        currentRoute = currentRoute,
                        onNavigate = { route -> navController.navigateTab(route) },
                    )
                }
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
            ) {
                content()
            }
        }
    }
}

@Composable
fun ComingSoonScreen(title: String) {
    Column(Modifier.fillMaxSize()) {
        ScreenHeader(title)
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Em breve", color = Color(0xFF9A9FAD))
        }
    }
}
