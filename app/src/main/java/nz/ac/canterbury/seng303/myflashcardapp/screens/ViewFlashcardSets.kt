package nz.ac.canterbury.seng303.myflashcardapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.myflashcardapp.models.MultipleChoiceFlashcardSet
import nz.ac.canterbury.seng303.myflashcardapp.models.TraditionalFlashcardSet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewFlashcardScreen(
    navController: NavController,
    traditionalFlashcardSets: List<TraditionalFlashcardSet>,
    multipleChoiceFlashcardSets: List<MultipleChoiceFlashcardSet>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(80.dp),
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("View Flashcard Sets")
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
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Multiple Choice Flashcard Sets",
                        fontSize = 20.sp,  // Reduced font size
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        navController.navigate("multiple_choice_flashcard_screen")
                    }) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Multiple Choice Set")
                    }
                }
            }

            if (multipleChoiceFlashcardSets.isEmpty()) {
                item {
                    Text(
                        text = "You have no Multiple Choice Sets yet",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = Color.Gray
                    )
                }
            } else {
                items(multipleChoiceFlashcardSets) { flashcardSet ->
                    MultipleChoiceFlashcardSetItem(
                        title = flashcardSet.title,
                        navController = navController,
                        flashcardSet = flashcardSet
                    )
                    Divider()
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Traditional Flashcard Sets",
                        fontSize = 20.sp,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        navController.navigate("traditional_flashcard_screen")
                    }) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Traditional Set")
                    }
                }
            }

            if (traditionalFlashcardSets.isEmpty()) {
                item {
                    Text(
                        text = "You have no Traditional Sets yet",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = Color.Gray
                    )
                }
            } else {
                items(traditionalFlashcardSets) { flashcardSet ->
                    TraditionalFlashcardSetItem(
                        title = flashcardSet.title,
                        navController = navController,
                        flashcardSet = flashcardSet
                    )
                    Divider()
                }
            }
        }
    }
}

@Composable
fun MultipleChoiceFlashcardSetItem(
    title: String,
    navController: NavController,
    flashcardSet: MultipleChoiceFlashcardSet,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        IconButton(onClick = {
            navController.navigate("play_multiple_choice_flashcard_screen") {
                navController.currentBackStackEntry?.savedStateHandle?.set("flashcardSet", flashcardSet)
            }
        }) {
            Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "Play")
        }
    }
}

@Composable
fun TraditionalFlashcardSetItem(
    title: String,
    navController: NavController,
    flashcardSet: TraditionalFlashcardSet,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        IconButton(onClick = {
            navController.navigate("play_traditional_flashcard_screen") {
                navController.currentBackStackEntry?.savedStateHandle?.set("flashcardSet", flashcardSet)
            }
        }) {
            Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "Play")
        }
    }
}
