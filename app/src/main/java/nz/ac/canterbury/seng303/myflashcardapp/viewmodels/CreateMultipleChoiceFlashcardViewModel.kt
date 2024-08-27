package nz.ac.canterbury.seng303.myflashcardapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.myflashcardapp.datastore.Storage
import nz.ac.canterbury.seng303.myflashcardapp.models.MultipleChoiceFlashcardSet
import kotlin.random.Random
import kotlinx.coroutines.flow.first
import nz.ac.canterbury.seng303.myflashcardapp.models.MultipleChoiceFlashcard

class CreateMultipleChoiceFlashcardViewModel(
    private val multipleChoiceFlashcardStorage: Storage<MultipleChoiceFlashcardSet>
) : ViewModel() {

    private val _flashcardSets = MutableStateFlow<List<MultipleChoiceFlashcardSet>>(emptyList())
    val flashcardSets: StateFlow<List<MultipleChoiceFlashcardSet>> get() = _flashcardSets

    fun getFlashcardSets() = viewModelScope.launch {
        multipleChoiceFlashcardStorage.getAll()
            .catch { /* Handle the error appropriately */ }
            .collectLatest { sets ->
                _flashcardSets.value = sets
            }
    }

    fun saveMultipleChoiceFlashcardSet(title: String, flashcards: List<MultipleChoiceFlashcard>) = viewModelScope.launch {
        val flashcardSet = MultipleChoiceFlashcardSet(
            id = Random.nextInt(0, Int.MAX_VALUE),
            title = title,
            flashcards = flashcards
        )

        multipleChoiceFlashcardStorage.insert(flashcardSet)
            .catch { /* Handle the error appropriately */ }
            .collectLatest {
                // Triggering a refresh after saving
                getFlashcardSets()
            }
    }

    fun deleteFlashcardSet(setId: Int) = viewModelScope.launch {
        multipleChoiceFlashcardStorage.delete(setId)
            .catch { /* Handle the error appropriately */ }
            .collectLatest {
                // Triggering a refresh after deleting
                getFlashcardSets()
            }
    }

    fun updateFlashcardSet(setId: Int, updatedSet: MultipleChoiceFlashcardSet) = viewModelScope.launch {
        multipleChoiceFlashcardStorage.edit(setId, updatedSet)
            .catch { /* Handle the error appropriately */ }
            .collectLatest {
                // Triggering a refresh after updating
                getFlashcardSets()
            }
    }
}
