package nz.ac.canterbury.seng303.myflashcardapp.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.myflashcardapp.models.TraditionalFlashcard
import nz.ac.canterbury.seng303.myflashcardapp.viewmodels.CreateTraditionalFlashcardViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTraditionalFlashcardSetScreen(
    navController: NavController,
    viewModel: CreateTraditionalFlashcardViewModel = koinViewModel()
) {
    var title by rememberSaveable { mutableStateOf("") }
    var flashcards by rememberSaveable { mutableStateOf(listOf(TraditionalFlashcard(0, "", ""))) }
    var hasAttemptedSave by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Flashcard Set") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("choose_flashcard_style")
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        hasAttemptedSave = true
                        when {
                            title.isBlank() -> {
                                Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT).show()
                            }
                            flashcards.any { it.term.isBlank() || it.definition.isBlank() } -> {
                                Toast.makeText(context, "All terms and definitions must be filled out", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                viewModel.saveFlashcardSet(title, flashcards)
                                navController.navigate("view_flashcards") {
                                    popUpTo("createFlashcardSet") { inclusive = true }
                                }
                            }
                        }
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    flashcards = flashcards.toMutableList().apply {
                        add(TraditionalFlashcard(0, "", ""))
                    }
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Flashcard")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Flashcard Set Title") },
                modifier = Modifier.fillMaxWidth(),
                isError = hasAttemptedSave && title.isBlank()
            )

            Spacer(modifier = Modifier.height(16.dp))

            flashcards.forEachIndexed { index, flashcard ->
                FlashcardInput(
                    flashcard = flashcard,
                    onRemove = {
                        if (flashcards.size > 1) {
                            flashcards = flashcards.toMutableList().apply { removeAt(index) }
                        } else {
                            Toast.makeText(context, "You must have at least one flashcard", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onTermChange = { newTerm ->
                        flashcards = flashcards.toMutableList().apply {
                            this[index] = this[index].copy(term = newTerm)
                        }
                    },
                    onDefinitionChange = { newDefinition ->
                        flashcards = flashcards.toMutableList().apply {
                            this[index] = this[index].copy(definition = newDefinition)
                        }
                    },
                    showError = hasAttemptedSave
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun FlashcardInput(
    flashcard: TraditionalFlashcard,
    onRemove: () -> Unit,
    onTermChange: (String) -> Unit,
    onDefinitionChange: (String) -> Unit,
    showError: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        val context = LocalContext.current
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            IconButton(
                onClick = {
                    if (flashcard.term.isBlank()) {
                        Toast.makeText(context, "Please enter a term before searching", Toast.LENGTH_SHORT).show()
                    } else {
                        openWebSearch(context, flashcard.term)
                    }
                }
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search Term")
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Close, contentDescription = "Remove Flashcard")
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            OutlinedTextField(
                value = flashcard.term,
                onValueChange = onTermChange,
                label = { Text("Term") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                isError = showError && flashcard.term.isBlank()
            )
            OutlinedTextField(
                value = flashcard.definition,
                onValueChange = onDefinitionChange,
                label = { Text("Definition") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                isError = showError && flashcard.definition.isBlank()
            )
        }
    }
}

fun openWebSearch(context: android.content.Context, query: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=$query"))
    context.startActivity(intent)
}