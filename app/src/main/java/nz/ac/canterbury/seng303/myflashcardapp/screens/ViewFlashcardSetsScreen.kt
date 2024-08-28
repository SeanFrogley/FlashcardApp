package nz.ac.canterbury.seng303.myflashcardapp.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewFlashcardSetsScreen(
    navController: NavController,
    viewModel: ViewFlashcardSetsViewModel = koinViewModel()
) {
    val multipleChoiceFlashcardSets by viewModel.multipleChoiceFlashcardSets.collectAsState()
    val traditionalFlashcardSets by viewModel.traditionalFlashcardSets.collectAsState()
    Log.d("ViewFlashcardSetsScreen", "TraditionalFlashcardSets: $traditionalFlashcardSets")
    Log.d("ViewFlashcardSetsScreen", "MultipleChoiceFlashcardSets: $multipleChoiceFlashcardSets")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("View Flashcard Sets") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
                        // Navigate to the create multiple choice flashcard set screen
                        // navController.navigate("createMultipleChoiceFlashcardSet")
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
                        onPlayClick = {
                            // Navigate to a screen to view/play the specific flashcard set
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
                        onPlayClick = {
                            // Navigate to a screen to view/play the specific flashcard set
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
    onPlayClick: () -> Unit // Callback for the play button
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title)
            // Additional content can be added here
        }
        // Play Button
        IconButton(onClick = onPlayClick) {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play Set")
        }
        // Edit Button
        IconButton(onClick = { /* Implement edit functionality later */ }) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Set")
        }
        // Delete Button
        IconButton(onClick = { /* Implement delete functionality later */ }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Set")
        }
    }
}
