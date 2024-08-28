package nz.ac.canterbury.seng303.myflashcardapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
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
import nz.ac.canterbury.seng303.myflashcardapp.viewmodels.ViewMultipleChoiceFlashcardViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewMultipleChoiceFlashcardScreen(
    navController: NavController,
    setId: Int,
    viewModel: ViewMultipleChoiceFlashcardViewModel = koinViewModel()
) {
    viewModel.loadFlashcardSet(setId)

    val flashcardSet by viewModel.flashcardSet.collectAsState()
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
                            text = "Title: ${flashcardSet?.title ?: "Loading..."}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { /* do nothing, this button is just to balance the title */ },
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
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                flashcardSet?.flashcards?.forEachIndexed { index, flashcard ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = "Question: ${flashcard.question}",
                            fontSize = 18.sp,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                if (flashcard.question.isBlank()) {
                                    Toast.makeText(context, "Question is empty, nothing to search", Toast.LENGTH_SHORT).show()
                                } else {
                                    openWebSearch(context, flashcard.question)
                                }
                            }
                        ) {
                            Icon(Icons.Default.Search, contentDescription = "Search Question")
                        }
                    }

                    flashcard.options.forEachIndexed { optionIndex, option ->
                        Text(
                            text = "Option ${optionIndex + 1}: ${option.text} ${if (option.isCorrect) "(Correct)" else ""}",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 32.dp, bottom = 8.dp)
                        )
                    }
                    Divider()
                }
            }
        }
    )
}
