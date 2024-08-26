package nz.ac.canterbury.seng303.myflashcardapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultipleChoiceFlashcardScreen(navController: NavController, modifier: Modifier = Modifier) {
    var flashcardSetTitle by remember { mutableStateOf("") }
    var flashcardSetTitleError by remember { mutableStateOf(false) }
    var flashcards by remember { mutableStateOf(listOf(generateDefaultFlashcard())) }
    var flashcardErrors by remember { mutableStateOf(List(flashcards.size) { MultipleChoiceFlashcardErrorState() }) }

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

                            // Validate Title
                            if (flashcardSetTitle.isBlank()) {
                                flashcardSetTitleError = true
                                hasError = true
                            } else {
                                flashcardSetTitleError = false
                            }

                            // Validate each flashcard
                            flashcardErrors = flashcards.map { flashcard ->
                                val questionError = flashcard.question.isBlank()
                                val optionErrors = flashcard.options.map { it.text.isBlank() }
                                val correctAnswerError = flashcard.options.none { it.isCorrect } // Check if a correct answer is selected
                                if (questionError || optionErrors.any { it } || correctAnswerError) hasError = true
                                MultipleChoiceFlashcardErrorState(questionError, optionErrors, correctAnswerError)
                            }

                            // If no errors, proceed to save flashcards
                            if (!hasError) {
                                // Save flashcard
                            }
                        },
                    )
                    {
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
                                    add(generateDefaultFlashcard())
                                }
                                flashcardErrors = flashcardErrors + MultipleChoiceFlashcardErrorState()
                            },
                            modifier = Modifier.fillMaxSize()
                                .size(10.dp)
                                .padding(5.dp),
                            shape = CircleShape
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add Flashcard")
                        }
                    }
                }
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                item {
                    // Title field is now part of the scrolling content
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
                }

                items(flashcards.size) { index ->
                    MultipleChoiceFlashcardItem(
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
    )
}

@Composable
fun MultipleChoiceFlashcardItem(
    flashcard: FlashcardData,
    onUpdateFlashcard: (FlashcardData) -> Unit,
    onRemoveFlashcard: () -> Unit,
    flashcardErrorState: MultipleChoiceFlashcardErrorState
) {
    var questionText by remember { mutableStateOf(flashcard.question) }
    var options by remember { mutableStateOf(flashcard.options) }

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

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {
                    // No implementation, just adding the button
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        }


        TextField(
            value = questionText,
            onValueChange = {
                questionText = it
                onUpdateFlashcard(flashcard.copy(question = questionText, options = options))
            },
            label = { Text("Question") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = flashcardErrorState.questionError
        )
        if (flashcardErrorState.questionError) {
            Text(
                text = "Question can't be empty",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (flashcardErrorState.optionErrors.size != options.size) {
            flashcardErrorState.optionErrors = options.map { false }
        }

        options.forEachIndexed { index, option ->
            OptionItem(
                option = option,
                onOptionTextChange = { newText ->
                    options = options.toMutableList().apply {
                        this[index] = this[index].copy(text = newText)
                    }
                    onUpdateFlashcard(flashcard.copy(question = questionText, options = options))
                },
                onOptionSelected = {
                    options = options.mapIndexed { i, opt ->
                        opt.copy(isCorrect = i == index)
                    }.toMutableList()
                    onUpdateFlashcard(flashcard.copy(question = questionText, options = options))
                },
                onRemoveOption = {
                    if (options.size > 2) {
                        options = options.toMutableList().apply {
                            removeAt(index)
                        }
                    }
                    onUpdateFlashcard(flashcard.copy(question = questionText, options = options))
                },
                isError = flashcardErrorState.optionErrors[index]
            )
            if (flashcardErrorState.optionErrors[index]) {
                Text(
                    text = "Option can't be empty",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Display error if no correct answer is selected
        if (flashcardErrorState.correctAnswerError) {
            Text(
                text = "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        if (options.size < 4) {
            IconButton(
                onClick = {
                    options = options.toMutableList().apply {
                        add(OptionData("", false))
                    }
                    onUpdateFlashcard(flashcard.copy(question = questionText, options = options))
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Option",
                )
            }
        }
    }
}


data class FlashcardData(
    val question: String,
    val options: List<OptionData>
) {
    fun isValid() = question.isNotBlank() && options.all { it.text.isNotBlank() }
}

data class MultipleChoiceFlashcardErrorState(
    val questionError: Boolean = false,
    var optionErrors: List<Boolean> = listOf(),
    var correctAnswerError: Boolean = false
)

fun generateDefaultFlashcard(): FlashcardData {
    return FlashcardData(
        question = "",
        options = generateDefaultOptions(4)
    )
}

fun generateDefaultOptions(size: Int): MutableList<OptionData> {
    return MutableList(size) { OptionData("", false) }
}

data class OptionData(
    var text: String,
    var isCorrect: Boolean
)

@Composable
fun OptionItem(
    option: OptionData,
    onOptionTextChange: (String) -> Unit,
    onOptionSelected: () -> Unit,
    onRemoveOption: () -> Unit,
    isError: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = { onRemoveOption() }) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove Option"
            )
        }
        RadioButton(
            selected = option.isCorrect,
            onClick = { onOptionSelected() }
        )
        TextField(
            value = option.text,
            onValueChange = { onOptionTextChange(it) },
            label = { Text("Option") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = isError
        )
    }
}