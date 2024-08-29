package nz.ac.canterbury.seng303.myflashcardapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.myflashcardapp.datastore.MultipleChoiceFlashcardPersistentStorage
import nz.ac.canterbury.seng303.myflashcardapp.datastore.Storage
import nz.ac.canterbury.seng303.myflashcardapp.models.MultipleChoiceFlashcardSet
import nz.ac.canterbury.seng303.myflashcardapp.models.TraditionalFlashcardSet

class ViewMultipleChoiceFlashcardViewModel(
    private val multipleChoiceFlashcardPersistentStorage: Storage<MultipleChoiceFlashcardSet>
) : ViewModel() {

    private val _flashcardSet = MutableStateFlow<MultipleChoiceFlashcardSet?>(null)
    val flashcardSet: StateFlow<MultipleChoiceFlashcardSet?> get() = _flashcardSet

    fun loadFlashcardSet(setId: Int) = viewModelScope.launch {
        multipleChoiceFlashcardPersistentStorage.get { it.id == setId }
            .catch { e ->
                // Handle error
            }
            .collect { set ->
                _flashcardSet.value = set
            }
    }

    fun deleteFlashcard(setId: Int, index: Int) = viewModelScope.launch {
        val currentSet = _flashcardSet.value ?: return@launch
        multipleChoiceFlashcardPersistentStorage.deleteFlashcardFromSet(setId, index).collect { result ->
            if (result == 1) {
                val updatedFlashcards = currentSet.flashcards.toMutableList().apply {
                    removeAt(index)
                }
                _flashcardSet.value = currentSet.copy(flashcards = updatedFlashcards)
            } else {
                // Handle deletion failure
            }
        }
    }

}
