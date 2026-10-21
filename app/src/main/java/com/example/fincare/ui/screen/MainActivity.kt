package com.example.fincare.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fincare.ui.screen.thread.test.ContactExploreScreen
import com.example.fincare.ui.theme.FincareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            FincareApp {
                NavHost(
                    navController = navController,
                    startDestination = "exploreScreen"
                ) {
                    composable("exploreScreen") {
//                        ExploreScreen(navController = navController)
                        ContactExploreScreen(navController = navController)
                    }

                    composable("detailScreen")  {
//                        DetailScreen()
                            ContactFormScreen()
                    }
                }
            }
        }
    }

}

@Composable
fun FincareApp(
    content: @Composable () -> Unit
) {
    FincareTheme() {
        Scaffold { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = innerPadding.calculateTopPadding()
                    )
            ) {
                content()
            }
        }
    }
}



