package nz.ac.canterbury.seng303.myflashcardapp.viewmodels

import android.util.Log
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
import kotlinx.coroutines.flow.firstOrNull
import nz.ac.canterbury.seng303.myflashcardapp.models.MultipleChoiceFlashcard

class CreateMultipleChoiceFlashcardViewModel(
    private val multipleChoiceFlashcardStorage: Storage<MultipleChoiceFlashcardSet>
) : ViewModel() {

    private val _flashcardSets = MutableStateFlow<List<MultipleChoiceFlashcardSet>>(emptyList())
    val flashcardSets: StateFlow<List<MultipleChoiceFlashcardSet>> get() = _flashcardSets


    fun saveMultipleChoiceFlashcardSet(title: String, flashcards: List<MultipleChoiceFlashcard>) = viewModelScope.launch {
        val flashcardSet = MultipleChoiceFlashcardSet(
            id = Random.nextInt(0, Int.MAX_VALUE),
            title = title,
            flashcards = flashcards
        )
        Log.d("DataStoreInsert", "Inserting set: $flashcards")
        multipleChoiceFlashcardStorage.insert(flashcardSet)
            .catch { Log.e("MULTIPLE_CHOICE_VM", "Could not insert multiple-choice flashcard set: $it") }
            .collect {
                Log.d("MULTIPLE_CHOICE_VM", "Multiple-choice flashcard set inserted successfully")
                _flashcardSets.value = _flashcardSets.value + flashcardSet
            }
    }


    fun deleteFlashcardSet(setId: Int) = viewModelScope.launch {
        multipleChoiceFlashcardStorage.delete(setId)
            .catch { /* Handle the error appropriately */ }
            .collectLatest {
                getFlashcardSets()
            }
    }

    fun getFlashcardSets() = viewModelScope.launch {
        multipleChoiceFlashcardStorage.getAll()
            .catch { /* Handle the error appropriately */ }
            .collectLatest { sets ->
                _flashcardSets.value = sets
            }
    }

    fun updateFlashcardSet(setId: Int, updatedSet: MultipleChoiceFlashcardSet) = viewModelScope.launch {
        multipleChoiceFlashcardStorage.edit(setId, updatedSet)
            .catch { /* Handle the error appropriately */ }
            .collectLatest {
                getFlashcardSets()
            }
    }
}
