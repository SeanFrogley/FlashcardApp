package nz.ac.canterbury.seng303.myflashcardapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TraditionalFlashcardScreen(navController: NavController, modifier: Modifier = Modifier) {
    var flashcardSetTitle by remember { mutableStateOf("") }
    var flashcardSetTitleError by remember { mutableStateOf(false) }
    var flashcards by remember { mutableStateOf(listOf(generateDefaultTraditionalFlashcard())) }
    var flashcardErrors by remember { mutableStateOf(List(flashcards.size) { TraditionalFlashcardErrorState() }) }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(75.dp),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text("Create Flashcard Set")
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
                        onClick = {
                            var hasError = false

                            if (flashcardSetTitle.isBlank()) {
                                flashcardSetTitleError = true
                                hasError = true
                            } else {
                                flashcardSetTitleError = false
                            }

                            flashcardErrors = flashcards.map { flashcard ->
                                val termError = flashcard.term.isBlank()
                                val definitionError = flashcard.definition.isBlank()
                                if (termError || definitionError) hasError = true
                                TraditionalFlashcardErrorState(termError, definitionError)
                            }

                            if (!hasError) {
                                // Save flashcards (implement saving logic)
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Finish"
                        )
                    }
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(70.dp),
                content = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        FloatingActionButton(
                            onClick = {
                                flashcards = flashcards.toMutableList().apply {
                                    add(generateDefaultTraditionalFlashcard())
                                }
                                flashcardErrors = flashcardErrors + TraditionalFlashcardErrorState()
                            },
                            shape = CircleShape
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add Flashcard")
                        }
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                TextField(
                    value = flashcardSetTitle,
                    onValueChange = { flashcardSetTitle = it },
                    label = { Text("Flashcard Set Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = flashcardSetTitleError
                )
                if (flashcardSetTitleError) {
                    Text(
                        text = "Flashcard Set Title can't be empty",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 16.dp)
                ) {
                    items(flashcards.size) { index ->
                        TraditionalFlashcardItem(
                            flashcard = flashcards[index],
                            onUpdateFlashcard = { updatedFlashcard ->
                                flashcards = flashcards.toMutableList().apply {
                                    this[index] = updatedFlashcard
                                }
                            },
                            onRemoveFlashcard = {
                                flashcards = flashcards.toMutableList().apply {
                                    removeAt(index)
                                }
                                flashcardErrors = flashcardErrors.toMutableList().apply {
                                    removeAt(index)
                                }
                            },
                            flashcardErrorState = flashcardErrors[index]
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    )
}

@Composable
fun TraditionalFlashcardItem(
    flashcard: TraditionalFlashcardData,
    onUpdateFlashcard: (TraditionalFlashcardData) -> Unit,
    onRemoveFlashcard: () -> Unit,
    flashcardErrorState: TraditionalFlashcardErrorState
) {
    var termText by remember { mutableStateOf(flashcard.term) }
    var definitionText by remember { mutableStateOf(flashcard.definition) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = {
                    onRemoveFlashcard()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove Flashcard"
                )
            }
        }

        TextField(
            value = termText,
            onValueChange = {
                termText = it
                onUpdateFlashcard(flashcard.copy(term = termText, definition = definitionText))
            },
            label = { Text("Term") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = flashcardErrorState.termError
        )
        if (flashcardErrorState.termError) {
            Text(
                text = "Term can't be empty",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = definitionText,
            onValueChange = {
                definitionText = it
                onUpdateFlashcard(flashcard.copy(term = termText, definition = definitionText))
            },
            label = { Text("Definition") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            isError = flashcardErrorState.definitionError
        )
        if (flashcardErrorState.definitionError) {
            Text(
                text = "Definition can't be empty",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

data class TraditionalFlashcardData(
    val term: String,
    val definition: String
) {
    fun isValid() = term.isNotBlank() && definition.isNotBlank()
}

data class TraditionalFlashcardErrorState(
    val termError: Boolean = false,
    val definitionError: Boolean = false
)

fun generateDefaultTraditionalFlashcard(): TraditionalFlashcardData {
    return TraditionalFlashcardData(
        term = "",
        definition = ""
    )
}
