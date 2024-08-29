package nz.ac.canterbury.seng303.myflashcardapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.myflashcardapp.datastore.Storage
import nz.ac.canterbury.seng303.myflashcardapp.models.MultipleChoiceFlashcardSet

class PlayMultipleChoiceFlashcardViewModel(
    private val multipleChoiceFlashcardStorage: Storage<MultipleChoiceFlashcardSet>
) : ViewModel() {

    private val _flashcardSet = MutableStateFlow<MultipleChoiceFlashcardSet?>(null)
    val flashcardSet: StateFlow<MultipleChoiceFlashcardSet?> get() = _flashcardSet

    fun loadFlashcardSet(setId: Int) = viewModelScope.launch {
        multipleChoiceFlashcardStorage.get { it.id == setId }
            .catch { e ->
                // Handle error
            }
            .collect { set ->
                _flashcardSet.value = set
            }
    }
    fun updateSingleFlashcard(setId: Int, flashcardIndex: Int, gotCorrect: Boolean) = viewModelScope.launch {
        val currentSet = _flashcardSet.value ?: return@launch
        val updatedFlashcards = currentSet.flashcards.toMutableList()
        val updatedFlashcard = updatedFlashcards[flashcardIndex].copy(gotCorrect = gotCorrect)
        updatedFlashcards[flashcardIndex] = updatedFlashcard

        val updatedSet = currentSet.copy(flashcards = updatedFlashcards)
        multipleChoiceFlashcardStorage.edit(setId, updatedSet).collect {
            _flashcardSet.value = updatedSet
        }
    }
}
