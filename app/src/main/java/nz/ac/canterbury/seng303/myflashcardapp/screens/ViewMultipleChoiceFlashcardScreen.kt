package nz.ac.canterbury.seng303.myflashcardapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
    // Load the flashcard set data
    viewModel.loadFlashcardSet(setId)

    // Collect the flashcard set from the ViewModel
    val flashcardSet by viewModel.flashcardSet.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = "Title: ${flashcardSet?.title ?: "Loading..."}",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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

                // Display each flashcard's question and options
                flashcardSet?.flashcards?.forEachIndexed { index, flashcard ->
                    Text(
                        text = "Question: ${flashcard.question}",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    flashcard.options.forEachIndexed { optionIndex, option ->
                        Text(
                            text = "Option ${optionIndex + 1}: ${option.text} ${if (option.isCorrect) "(Correct)" else ""}",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 32.dp, bottom = 8.dp)
                        )
                    }
                    Divider() // Adds a divider between flashcards for clarity
                }
            }
        }
    )
}
