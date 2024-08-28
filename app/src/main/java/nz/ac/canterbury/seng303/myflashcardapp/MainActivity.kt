package nz.ac.canterbury.seng303.myflashcardapp

import CreateMultipleChoiceFlashcardSetScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import nz.ac.canterbury.seng303.myflashcardapp.screens.*
import nz.ac.canterbury.seng303.myflashcardapp.ui.theme.MyFlashcardAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyFlashcardAppTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "main_screen",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("main_screen") {
                            MainScreen(navController = navController)
                        }
                        composable("choose_flashcard_style") {
                            ChooseFlashCardStyle(navController = navController)
                        }
                        composable("view_flashcards") {
                            ViewFlashcardSetsScreen(navController)
                        }
                        composable("play_multiple_choice_flashcard_screen") {
                            PlayMultipleChoiceFlashcardScreen(navController = navController)
                        }
                        composable("play_traditional_flashcard_screen") {
                            PlayTraditionalFlashcardScreen(navController = navController)
                        }
                        composable("create_multiple_choice_flashcard_screen") {
                            CreateMultipleChoiceFlashcardSetScreen(navController)
                        }
                        composable("create_traditional_flashcard_screen") {
                            CreateTraditionalFlashcardSetScreen(navController)
                        }
                        composable("view_traditional_flashcard_screen/{setId}") { backStackEntry ->
                            val setId = backStackEntry.arguments?.getString("setId")?.toIntOrNull() ?: return@composable
                            ViewTraditionalFlashcardScreen(navController, setId)
                        }
                        composable("view_multiple_choice_flashcard_screen/{setId}") { backStackEntry ->
                            val setId = backStackEntry.arguments?.getString("setId")?.toIntOrNull() ?: return@composable
                            ViewMultipleChoiceFlashcardScreen(navController, setId)
                        }

                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MyFlashcardAppTheme {
        MainScreen(navController = rememberNavController())
    }
}


