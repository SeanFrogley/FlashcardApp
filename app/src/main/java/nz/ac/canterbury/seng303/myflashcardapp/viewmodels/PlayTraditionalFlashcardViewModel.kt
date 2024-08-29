package nz.ac.canterbury.seng303.myflashcardapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.myflashcardapp.datastore.Storage
import nz.ac.canterbury.seng303.myflashcardapp.models.TraditionalFlashcard
import nz.ac.canterbury.seng303.myflashcardapp.models.TraditionalFlashcardSet

class PlayTraditionalFlashcardViewModel(
    private val traditionalFlashcardStorage: Storage<TraditionalFlashcardSet>
) : ViewModel() {

    private val _flashcardSet = MutableStateFlow<TraditionalFlashcardSet?>(null)
    val flashcardSet: StateFlow<TraditionalFlashcardSet?> get() = _flashcardSet

    fun loadFlashcardSet(setId: Int) = viewModelScope.launch {
        traditionalFlashcardStorage.get { it.id == setId }
            .catch { e ->
                // Handle error
            }
            .collect { set ->
                _flashcardSet.value = set
            }
    }
    fun updateFlashcard(setId: Int, index: Int, updatedFlashcard: TraditionalFlashcard) = viewModelScope.launch {
        val currentSet = _flashcardSet.value ?: return@launch
        if (index !in currentSet.flashcards.indices) return@launch

        val updatedFlashcards = currentSet.flashcards.toMutableList().apply {
            this[index] = updatedFlashcard
        }

        val updatedSet = currentSet.copy(flashcards = updatedFlashcards)
        traditionalFlashcardStorage.edit(setId, updatedSet).collect {
            _flashcardSet.value = updatedSet
        }
    }

}
