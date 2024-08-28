package nz.ac.canterbury.seng303.myflashcardapp.screens

import android.widget.Toast
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.myflashcardapp.viewmodels.ViewTraditionalFlashcardViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewTraditionalFlashcardScreen(
    navController: NavController,
    setId: Int,
    viewModel: ViewTraditionalFlashcardViewModel = koinViewModel()
) {
    viewModel.loadFlashcardSet(setId)

    val flashcardSet by viewModel.flashcardSet.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(flashcardSet) {
        Log.d("PlayTraditionalFlashcardScreen", "Loaded Flashcard Set: $flashcardSet")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(80.dp),
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Title: ${flashcardSet?.title ?: "Loading..."}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { /* do nothing, this button is just to balance the title */ },
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
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                flashcardSet?.flashcards?.forEachIndexed { index, flashcard ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Term: ${flashcard.term}",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                            Text(
                                text = "Definition: ${flashcard.definition}",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                            )
                        }
                        IconButton(
                            onClick = {
                                if (flashcard.term.isBlank()) {
                                    Toast.makeText(context, "Term is empty, nothing to search", Toast.LENGTH_SHORT).show()
                                } else {
                                    openWebSearch(context, flashcard.term)
                                }
                            }
                        ) {
                            Icon(Icons.Default.Search, contentDescription = "Search Term")
                        }
                        IconButton(
                            onClick = {
                                Toast.makeText(context, "Deleted: ${flashcard.term}", Toast.LENGTH_SHORT).show()
                                // viewModel.deleteFlashcard(setId, index)
                            }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Term", tint = Color.Red)
                        }
                    }
                    Divider()
                }
            }
        }
    )
}
