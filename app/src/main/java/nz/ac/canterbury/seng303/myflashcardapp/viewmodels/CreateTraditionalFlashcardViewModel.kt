package nz.ac.canterbury.seng303.myflashcardapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import nz.ac.canterbury.seng303.myflashcardapp.datastore.Storage
import nz.ac.canterbury.seng303.myflashcardapp.models.TraditionalFlashcardSet
import nz.ac.canterbury.seng303.myflashcardapp.models.TraditionalFlashcard
import android.util.Log
import kotlin.random.Random

class CreateTraditionalFlashcardViewModel(
    private val traditionalFlashcardStorage: Storage<TraditionalFlashcardSet>
) : ViewModel() {

    private val _flashcardSets = MutableStateFlow<List<TraditionalFlashcardSet>>(emptyList())
    val flashcardSets: StateFlow<List<TraditionalFlashcardSet>> get() = _flashcardSets

    private val _selectedFlashcardSet = MutableStateFlow<TraditionalFlashcardSet?>(null)
    val selectedFlashcardSet: StateFlow<TraditionalFlashcardSet?> = _selectedFlashcardSet

    init {
        // Automatically load flashcard sets when the ViewModel is initialized
        loadFlashcardSets()
    }

    private fun loadFlashcardSets() = viewModelScope.launch {
        traditionalFlashcardStorage.getAll()
            .catch { Log.e("FLASHCARD_VIEW_MODEL", "Error loading flashcard sets: $it") }
            .collect { sets -> _flashcardSets.value = sets }
    }

    fun loadDefaultFlashcardSetsIfNoneExist() = viewModelScope.launch {
        val currentSets = traditionalFlashcardStorage.getAll().first()
        if (currentSets.isEmpty()) {
            Log.d("FLASHCARD_VIEW_MODEL", "Inserting default flashcard sets...")
            val defaultSets = getDefaultFlashcardSets()
            traditionalFlashcardStorage.insertAll(defaultSets)
                .catch { Log.w("FLASHCARD_VIEW_MODEL", "Could not insert default flashcard sets: $it") }
                .collect {
                    Log.d("FLASHCARD_VIEW_MODEL", "Default flashcard sets inserted successfully")
                    _flashcardSets.value = defaultSets
                }
        }
    }

    fun saveFlashcardSet(title: String, flashcards: List<TraditionalFlashcard>) = viewModelScope.launch {
        val flashcardSet = TraditionalFlashcardSet(
            id = Random.nextInt(0, Int.MAX_VALUE),
            title = title,
            flashcards = flashcards
        )

        traditionalFlashcardStorage.insert(flashcardSet)
            .catch { Log.e("FLASHCARD_VIEW_MODEL", "Could not insert flashcard set: $it") }
            .collect {
                Log.d("FLASHCARD_VIEW_MODEL", "Flashcard set inserted successfully")
                _flashcardSets.value = _flashcardSets.value + flashcardSet
            }
    }

    private fun getDefaultFlashcardSets(): List<TraditionalFlashcardSet> {
        // Implement this method to return a list of default flashcard sets
        return emptyList() // Placeholder
    }
}

