package nz.ac.canterbury.seng303.myflashcardapp.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.myflashcardapp.models.TraditionalFlashcardSet
import nz.ac.canterbury.seng303.myflashcardapp.viewmodels.ViewFlashcardSetsViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewFlashcardSetsScreen(
    navController: NavController,
    viewModel: ViewFlashcardSetsViewModel = koinViewModel()
) {
    val multipleChoiceFlashcardSets by viewModel.multipleChoiceFlashcardSets.collectAsState()
    val traditionalFlashcardSets by viewModel.traditionalFlashcardSets.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(80.dp),
                title = {
                    Text(
                        text = "View Sets",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("main_screen")
                    }) {
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
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Multiple Choice Flashcard Sets Section
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Multiple Choice Flashcard Sets",
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        navController.navigate("create_multiple_choice_flashcard_screen")
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Multiple Choice Set")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (multipleChoiceFlashcardSets.isEmpty()) {
                item {
                    Text(
                        text = "You have no Multiple Choice Sets yet",
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            } else {
                items(multipleChoiceFlashcardSets) { flashcardSet ->
                    FlashcardSetItem(
                        title = flashcardSet.title,
                        onEditClick = {
                            navController.navigate("edit_multiple_choice_flashcard_screen/${flashcardSet.id}")
                        },
                        onDeleteClick = { viewModel.deleteMultipleChoiceFlashcardSet(flashcardSet.id) },
                        onViewClick = {
                            navController.navigate("view_multiple_choice_flashcard_screen/${flashcardSet.id}")
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Traditional Flashcard Sets Section
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Traditional Flashcard Sets",
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        navController.navigate("create_traditional_flashcard_screen")
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Flashcard Set")
                    }
                }
            }

            if (traditionalFlashcardSets.isEmpty()) {
                item {
                    Text(
                        text = "You have no Traditional Flashcard Sets yet",
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            } else {
                items(traditionalFlashcardSets) { flashcardSet ->
                    FlashcardSetItem(
                        title = flashcardSet.title,
                        onEditClick = {
                            navController.navigate("edit_traditional_flashcard_screen/${flashcardSet.id}")
                        },
                        onDeleteClick = { viewModel.deleteTraditionalFlashcardSet(flashcardSet.id) },
                        onViewClick = {
                            navController.navigate("view_traditional_flashcard_screen/${flashcardSet.id}")
                        }
                    )
                }
            }
        }
    }
}




@Composable
fun FlashcardSetItem(
    title: String,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onViewClick: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title)
        }
        // Edit Button
        IconButton(onClick = onEditClick) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Set")
        }
        // Delete Button
        IconButton(onClick = { showDialog = true }) {
            Icon(imageVector = Icons.Default.Delete, tint = Color.Red, contentDescription = "Delete Set")
        }
        // View Button
        IconButton(onClick = onViewClick) {
            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "View Set")
        }
    }

    if (showDialog) {
        ConfirmDeleteDialog(
            onConfirm = onDeleteClick,
            onDismiss = { showDialog = false }
        )
    }
}




@Composable
fun ConfirmDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Confirm Deletion")
        },
        text = {
            Text("Are you sure you want to delete this flashcard set? This action cannot be undone.")
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
