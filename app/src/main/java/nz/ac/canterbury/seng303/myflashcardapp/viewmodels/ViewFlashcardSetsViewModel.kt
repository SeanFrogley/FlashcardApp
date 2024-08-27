package nz.ac.canterbury.seng303.myflashcardapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.myflashcardapp.datastore.Storage
import nz.ac.canterbury.seng303.myflashcardapp.models.TraditionalFlashcardSet

class ViewFlashcardSetsViewModel(
    private val traditionalFlashcardStorage: Storage<TraditionalFlashcardSet>
) : ViewModel() {

    private val _flashcardSets = MutableStateFlow<List<TraditionalFlashcardSet>>(emptyList())
    val flashcardSets: StateFlow<List<TraditionalFlashcardSet>> get() = _flashcardSets

    init {
        loadFlashcardSets()
    }

    private fun loadFlashcardSets() = viewModelScope.launch {
        traditionalFlashcardStorage.getAll()
            .catch { e -> // Handle the error
                // Log the error or handle it appropriately
            }
            .collect { sets ->
                _flashcardSets.emit(sets)
            }
    }
}
