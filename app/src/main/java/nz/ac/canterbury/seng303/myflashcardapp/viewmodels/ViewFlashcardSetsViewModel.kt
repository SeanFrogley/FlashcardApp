package nz.ac.canterbury.seng303.myflashcardapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.myflashcardapp.datastore.Storage
import nz.ac.canterbury.seng303.myflashcardapp.models.MultipleChoiceFlashcardSet
import nz.ac.canterbury.seng303.myflashcardapp.models.TraditionalFlashcardSet

class ViewFlashcardSetsViewModel(
    private val traditionalFlashcardStorage: Storage<TraditionalFlashcardSet>,
    private val multipleChoiceFlashcardStorage: Storage<MultipleChoiceFlashcardSet>
) : ViewModel() {

    private val _traditionalFlashcardSets = MutableStateFlow<List<TraditionalFlashcardSet>>(emptyList())
    val traditionalFlashcardSets: StateFlow<List<TraditionalFlashcardSet>> get() = _traditionalFlashcardSets

    private val _multipleChoiceFlashcardSets = MutableStateFlow<List<MultipleChoiceFlashcardSet>>(emptyList())
    val multipleChoiceFlashcardSets: StateFlow<List<MultipleChoiceFlashcardSet>> get() = _multipleChoiceFlashcardSets

    init {
        loadTraditionalFlashcardSets()
        loadMultipleChoiceFlashcardSets()
    }

    private fun loadTraditionalFlashcardSets() = viewModelScope.launch {
        traditionalFlashcardStorage.getAll()
            .catch { e -> // Handle the error
                // Log the error or handle it appropriately
            }
            .collect { sets ->
                _traditionalFlashcardSets.emit(sets)
            }
    }

    private fun loadMultipleChoiceFlashcardSets() = viewModelScope.launch {
        multipleChoiceFlashcardStorage.getAll()
            .catch { e -> // Handle the error
                // Log the error or handle it appropriately
            }
            .collect { sets ->
                _multipleChoiceFlashcardSets.emit(sets)
            }
    }
}

