package nz.ac.canterbury.seng303.myflashcardapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.myflashcardapp.datastore.Storage
import nz.ac.canterbury.seng303.myflashcardapp.models.MultipleChoiceFlashcard
import nz.ac.canterbury.seng303.myflashcardapp.models.MultipleChoiceFlashcardSet

class EditMultipleChoiceFlashcardViewModel(
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

    fun updateMultipleChoiceFlashcardSet(setId: Int, title: String, flashcards: List<MultipleChoiceFlashcard>) = viewModelScope.launch {
        val updatedSet = _flashcardSet.value?.copy(title = title, flashcards = flashcards) ?: return@launch
        multipleChoiceFlashcardStorage.edit(setId, updatedSet).collect { result ->
            if (result == 1) {
                _flashcardSet.value = updatedSet
            } else {
                // Handle update failure
            }
        }
    }
}
