package nz.ac.canterbury.seng303.myflashcardapp.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.myflashcardapp.models.MultipleChoiceFlashcardSet
import nz.ac.canterbury.seng303.myflashcardapp.models.MultipleChoiceOption
import nz.ac.canterbury.seng303.myflashcardapp.viewmodels.PlayMultipleChoiceFlashcardViewModel
import nz.ac.canterbury.seng303.myflashcardapp.viewmodels.PlayTraditionalFlashcardViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayMultipleChoiceFlashcardScreen(
    navController: NavController,
    setId: Int,
    viewModel: PlayMultipleChoiceFlashcardViewModel = koinViewModel()
) {
    LaunchedEffect(setId) {
        viewModel.loadFlashcardSet(setId)
    }

    val flashcardSet by viewModel.flashcardSet.collectAsState()

    var selectedOption by remember { mutableStateOf<MultipleChoiceOption?>(null) }
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    val totalQuestions = flashcardSet?.flashcards?.size ?: 1
    val context = LocalContext.current

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
                            text = "Question ${currentQuestionIndex + 1} of $totalQuestions",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = (currentQuestionIndex + 1) / totalQuestions.toFloat(),
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
                    Button(
                        onClick = {
                            val isCorrect = selectedOption?.isCorrect == true
                            flashcardSet?.let {
                                viewModel.updateSingleFlashcard(setId, currentQuestionIndex, isCorrect)
                            }

                            Toast.makeText(
                                context,
                                if (isCorrect) "Correct!" else "Incorrect!",
                                Toast.LENGTH_SHORT
                            ).show()

                            if (currentQuestionIndex < totalQuestions - 1) {
                                currentQuestionIndex++
                                selectedOption = null
                            } else {
                                navController.currentBackStackEntry?.savedStateHandle?.set("flashcardSet", flashcardSet)
                                navController.navigate("multiple_choice_results_screen/${flashcardSet?.id}")
                            }
                        },
                        enabled = selectedOption != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Submit")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (flashcardSet != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        text = flashcardSet!!.flashcards[currentQuestionIndex].question,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }

                items(flashcardSet!!.flashcards[currentQuestionIndex].options) { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                selectedOption = option
                            }
                            .background(if (selectedOption == option) Color.Cyan else Color.LightGray),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = option.text,
                                style = MaterialTheme.typography.bodyLarge,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No flashcard set available")
            }
        }
    }
}
