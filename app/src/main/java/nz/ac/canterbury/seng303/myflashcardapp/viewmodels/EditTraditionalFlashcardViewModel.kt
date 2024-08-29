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

class EditTraditionalFlashcardViewModel(
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

    fun updateFlashcardSet(setId: Int, title: String, flashcards: List<TraditionalFlashcard>) = viewModelScope.launch {
        val updatedSet = _flashcardSet.value?.copy(title = title, flashcards = flashcards)
        if (updatedSet != null) {
            traditionalFlashcardStorage.edit(setId, updatedSet).collect { result ->
                if (result != 1) {
                    // Handle update failure
                }
            }
        }
    }
}
