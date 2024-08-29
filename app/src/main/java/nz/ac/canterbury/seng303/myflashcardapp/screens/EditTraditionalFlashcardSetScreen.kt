package nz.ac.canterbury.seng303.myflashcardapp.screens

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
import nz.ac.canterbury.seng303.myflashcardapp.viewmodels.EditTraditionalFlashcardViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTraditionalFlashcardSetScreen(
    navController: NavController,
    setId: Int,
    viewModel: EditTraditionalFlashcardViewModel = koinViewModel()
) {
    var title by rememberSaveable { mutableStateOf("") }
    var flashcards by rememberSaveable { mutableStateOf(listOf(TraditionalFlashcard(0, "", ""))) }
    var hasAttemptedSave by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(setId) {
        viewModel.loadFlashcardSet(setId)
    }

    val flashcardSet by viewModel.flashcardSet.collectAsState()

    LaunchedEffect(flashcardSet) {
        flashcardSet?.let {
            title = it.title
            flashcards = it.flashcards
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Flashcard Set") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("view_flashcards")
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
                                viewModel.updateFlashcardSet(setId, title, flashcards)
                                navController.navigate("view_flashcards") {
                                    popUpTo("editFlashcardSet") { inclusive = true }
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

