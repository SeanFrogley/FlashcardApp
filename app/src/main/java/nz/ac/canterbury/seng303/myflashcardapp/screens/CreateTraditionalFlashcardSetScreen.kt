package nz.ac.canterbury.seng303.myflashcardapp.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.myflashcardapp.models.TraditionalFlashcard
import nz.ac.canterbury.seng303.myflashcardapp.viewmodels.CreateTraditionalFlashcardViewModel
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTraditionalFlashcardSetScreen(
    navController: NavController,
    viewModel: CreateTraditionalFlashcardViewModel = koinViewModel() // Use koinViewModel here
) {
    var title by remember { mutableStateOf("") }
    var flashcards by remember { mutableStateOf(mutableListOf<TraditionalFlashcard>()) }

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
                        viewModel.saveFlashcardSet(title, flashcards)
                        Log.d("CreateFlashcardSet", "Flashcard set saved successfully: $title with ${flashcards.size} flashcards")
                        navController.navigate("view_flashcards") {
                            popUpTo("createFlashcardSet") { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                }

            )
        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {
//                    flashcards.add(TraditionalFlashcard(0, "", ""))
//                },
//                modifier = Modifier.padding(16.dp)
//            ) {
//                Icon(Icons.Default.Add, contentDescription = "Add Flashcard")
//            }
//        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Flashcard Set Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            flashcards.forEachIndexed { index, flashcard ->
                FlashcardInput(
                    flashcard = flashcard,
                    onRemove = { flashcards.removeAt(index) },
                    onTermChange = { newTerm -> flashcards[index] = flashcards[index].copy(term = newTerm) },
                    onDefinitionChange = { newDefinition -> flashcards[index] = flashcards[index].copy(definition = newDefinition) }
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
    onDefinitionChange: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = onRemove) {
            Icon(Icons.Default.Close, contentDescription = "Remove Flashcard")
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
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )
            OutlinedTextField(
                value = flashcard.definition,
                onValueChange = onDefinitionChange,
                label = { Text("Definition") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
        }
    }
}
