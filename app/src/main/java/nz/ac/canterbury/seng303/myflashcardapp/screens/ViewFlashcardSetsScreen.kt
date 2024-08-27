package nz.ac.canterbury.seng303.myflashcardapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewFlashcardSetsScreen(
    navController: NavController,
    viewModel: ViewFlashcardSetsViewModel = koinViewModel()
) {
    val flashcardSets by viewModel.flashcardSets.collectAsState()

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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Multiple Choice Flashcard Sets Section
            Text(
                text = "Multiple Choice Flashcard Sets",
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "You have no Multiple Choice Sets yet",
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    // Navigate to the create multiple choice flashcard set screen
                    // navController.navigate("createMultipleChoiceFlashcardSet")
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Multiple Choice Set")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Traditional Flashcard Sets Section
            Text(
                text = "Traditional Flashcard Sets",
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (flashcardSets.isEmpty()) "You have no Traditional Sets yet" else "",
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    navController.navigate("createFlashcardSet")
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Flashcard Set")
                }
            }

            flashcardSets.forEach { flashcardSet ->
                FlashcardSetItem(flashcardSet) {
                    // Navigate to a screen to view/edit the specific flashcard set
                    // Example: navController.navigate("flashcardSetDetail/${flashcardSet.id}")
                }
            }
        }
    }
}

@Composable
fun FlashcardSetItem(
    flashcardSet: TraditionalFlashcardSet,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = flashcardSet.title)
            }
            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Go to Set")
        }
    }
}
