package nz.ac.canterbury.seng303.myflashcardapp.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.myflashcardapp.models.TraditionalFlashcardSet
import nz.ac.canterbury.seng303.myflashcardapp.viewmodels.PlayTraditionalFlashcardViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayTraditionalFlashcardScreen(
    navController: NavController,
    setId: Int,
    viewModel: PlayTraditionalFlashcardViewModel = koinViewModel()
) {
    // Load the flashcard set data
    viewModel.loadFlashcardSet(setId)

    // Collect the flashcard set from the ViewModel
    val flashcardSet by viewModel.flashcardSet.collectAsState()
    var showAnswer by remember { mutableStateOf(false) }
    var currentIndex by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    val totalQuestions = flashcardSet?.flashcards?.size ?: 1

    fun goToNextFlashcard() {
        if (currentIndex < totalQuestions - 1) {
            currentIndex++
            showAnswer = false
        } else {
            navController.currentBackStackEntry?.savedStateHandle?.set("flashcardSet", flashcardSet)
            navController.navigate("traditional_results_screen")
        }
    }

    fun markFlashcardAsCorrect(isCorrect: Boolean) {
        flashcardSet?.flashcards?.getOrNull(currentIndex)?.gotCorrect = isCorrect
        Toast.makeText(
            context,
            if (isCorrect) "Correct!" else "Incorrect!",
            Toast.LENGTH_SHORT
        ).show()
        goToNextFlashcard()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(80.dp),
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Question ${currentIndex + 1} of $totalQuestions",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = (currentIndex + 1) / totalQuestions.toFloat(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { /* do nothing this button is just to center the text */ },
                        enabled = false,
                        modifier = Modifier.alpha(0.3f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Check",
                            tint = Color.Transparent
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                content = {
                    if (showAnswer) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = {
                                    markFlashcardAsCorrect(false)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                            ) {
                                Text(text = "Wrong")
                            }
                            Button(
                                onClick = {
                                    markFlashcardAsCorrect(true)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                            ) {
                                Text(text = "Right")
                            }
                        }
                    } else {
                        Button(
                            onClick = { showAnswer = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(text = "Show Answer")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            flashcardSet?.let {
                val flashcard = it.flashcards.getOrNull(currentIndex)
                if (flashcard != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            text = flashcard.term,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        if (showAnswer) {
                            Text(
                                text = flashcard.definition,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                    }
                } else {
                    navController.currentBackStackEntry?.savedStateHandle?.set("flashcardSet", flashcardSet)
                    navController.navigate("navController.navigate(\"traditional_results_screen/${flashcardSet?.id}\")")
                }
            } ?: run {
                Text(text = "No flashcard set available", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
