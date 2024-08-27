package nz.ac.canterbury.seng303.myflashcardapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.myflashcardapp.models.MultipleChoiceOption
import nz.ac.canterbury.seng303.myflashcardapp.models.MultipleChoiceFlashcard

import android.content.Intent
import android.net.Uri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMultipleChoiceFlashcardSetScreen(
    navController: NavController
) {
    var title by remember { mutableStateOf("") }
    var flashcards by remember { mutableStateOf(mutableListOf(generateNewFlashcard())) }
    var selectedOptionIndices by remember { mutableStateOf(mutableListOf(-1)) }
    var hasAttemptedSave by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Flashcard Set") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        hasAttemptedSave = true
                        val hasErrors = validateFlashcards(flashcards, selectedOptionIndices)

                        if (title.isBlank()) {
                            Toast.makeText(context, "Flashcard Set Title cannot be empty", Toast.LENGTH_SHORT).show()
                        } else if (hasErrors) {
                            Toast.makeText(context, "Please correct the errors in the flashcards", Toast.LENGTH_SHORT).show()
                        } else {
                            // Save functionality will be implemented later
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
                    if (flashcards.size >= 4) {
                        Toast.makeText(context, "Cannot add more than 4 questions", Toast.LENGTH_SHORT).show()
                    } else {
                        flashcards = flashcards.toMutableList().apply { add(generateNewFlashcard()) }
                        selectedOptionIndices = selectedOptionIndices.toMutableList().apply { add(-1) }
                    }
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Question")
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

            flashcards.forEachIndexed { flashcardIndex, flashcard ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = {
                        if (flashcards.size > 1) {
                            flashcards = flashcards.toMutableList().apply { removeAt(flashcardIndex) }
                            selectedOptionIndices = selectedOptionIndices.toMutableList().apply { removeAt(flashcardIndex) }
                        } else {
                            Toast.makeText(context, "You need at least one flashcard", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Remove Question")
                    }
                    OutlinedTextField(
                        value = flashcard.question,
                        onValueChange = { questionText ->
                            flashcards = flashcards.toMutableList().apply {
                                this[flashcardIndex] = flashcard.copy(question = questionText)
                            }
                        },
                        label = { Text("Question") },
                        modifier = Modifier.weight(1f),
                        textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        isError = hasAttemptedSave && flashcard.question.isBlank()
                    )
                    IconButton(onClick = {
                        if (flashcard.question.isBlank()) {
                            Toast.makeText(context, "Question cannot be empty", Toast.LENGTH_SHORT).show()
                        } else {
                            openWebSearch(context, flashcard.question)
                        }
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Search Question")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                flashcard.options.forEachIndexed { optionIndex, option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = {
                            if (flashcard.options.size > 2) {
                                flashcards = flashcards.toMutableList().apply {
                                    val updatedOptions = flashcard.options.toMutableList().apply { removeAt(optionIndex) }
                                    this[flashcardIndex] = flashcard.copy(options = updatedOptions)
                                }
                            } else {
                                Toast.makeText(context, "A flashcard must have at least 2 options", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Remove Option")
                        }
                        OutlinedTextField(
                            value = option.text,
                            onValueChange = { optionText ->
                                flashcards = flashcards.toMutableList().apply {
                                    val updatedOptions = flashcard.options.toMutableList().apply {
                                        this[optionIndex] = option.copy(text = optionText)
                                    }
                                    this[flashcardIndex] = flashcard.copy(options = updatedOptions)
                                }
                            },
                            label = { Text("Option") },
                            modifier = Modifier.weight(1f),
                            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                            isError = hasAttemptedSave && option.text.isBlank()
                        )
                        RadioButton(
                            selected = selectedOptionIndices[flashcardIndex] == optionIndex,
                            onClick = {
                                selectedOptionIndices = selectedOptionIndices.toMutableList().apply {
                                    this[flashcardIndex] = optionIndex
                                }
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

fun generateNewFlashcard(): MultipleChoiceFlashcard {
    return MultipleChoiceFlashcard(
        id = 0,
        question = "",
        options = generateDefaultOptions(4)
    )
}

fun generateDefaultOptions(size: Int): MutableList<MultipleChoiceOption> {
    return MutableList(size) { MultipleChoiceOption("", false) }
}

fun validateFlashcards(
    flashcards: List<MultipleChoiceFlashcard>,
    selectedOptionIndices: List<Int>
): Boolean {
    var hasErrors = false

    flashcards.forEachIndexed { index, flashcard ->
        if (flashcard.question.isBlank()) {
            hasErrors = true
        }

        if (flashcard.options.size !in 2..4) {
            hasErrors = true
        }

        flashcard.options.forEach { option ->
            if (option.text.isBlank()) {
                hasErrors = true
            }
        }

        if (selectedOptionIndices[index] == -1) {
            hasErrors = true
        }
    }

    return hasErrors
}

fun openWebSearch(context: android.content.Context, query: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=$query"))
    context.startActivity(intent)
}
