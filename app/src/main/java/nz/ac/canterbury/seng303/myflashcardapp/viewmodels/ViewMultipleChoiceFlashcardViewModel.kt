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

}
