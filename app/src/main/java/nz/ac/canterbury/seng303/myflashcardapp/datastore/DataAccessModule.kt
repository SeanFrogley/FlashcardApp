package nz.ac.canterbury.seng303.myflashcardapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.FlowPreview
import nz.ac.canterbury.seng303.myflashcardapp.models.MultipleChoiceFlashcardSet
import nz.ac.canterbury.seng303.myflashcardapp.models.TraditionalFlashcardSet
import nz.ac.canterbury.seng303.myflashcardapp.viewmodels.CreateMultipleChoiceFlashcardViewModel
import nz.ac.canterbury.seng303.myflashcardapp.viewmodels.CreateTraditionalFlashcardViewModel
import nz.ac.canterbury.seng303.myflashcardapp.viewmodels.EditMultipleChoiceFlashcardViewModel
import nz.ac.canterbury.seng303.myflashcardapp.viewmodels.EditTraditionalFlashcardViewModel
import nz.ac.canterbury.seng303.myflashcardapp.viewmodels.MultipleChoiceResultsViewModel
import nz.ac.canterbury.seng303.myflashcardapp.viewmodels.PlayFlashcardSetsViewModel
import nz.ac.canterbury.seng303.myflashcardapp.viewmodels.PlayMultipleChoiceFlashcardViewModel
import nz.ac.canterbury.seng303.myflashcardapp.viewmodels.PlayTraditionalFlashcardViewModel
import nz.ac.canterbury.seng303.myflashcardapp.viewmodels.TraditionalResultsViewModel
import nz.ac.canterbury.seng303.myflashcardapp.viewmodels.ViewTraditionalFlashcardViewModel
import nz.ac.canterbury.seng303.myflashcardapp.viewmodels.ViewFlashcardSetsViewModel
import nz.ac.canterbury.seng303.myflashcardapp.viewmodels.ViewMultipleChoiceFlashcardViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named

private val Context.traditionalChoiceDataStore: DataStore<Preferences> by preferencesDataStore(name = "traditional_flashcard_data_sets")
private val Context.multipleChoiceDataStore: DataStore<Preferences> by preferencesDataStore(name = "multiple_choice_flashcard_data_sets")

@FlowPreview
val dataAccessModule = module {
    // Storage for TraditionalFlashcardSet
    single<Storage<TraditionalFlashcardSet>>(named("traditionalStorage")) {
        TraditionalFlashcardPersistentStorage(
            gson = get(),
            type = object : TypeToken<List<TraditionalFlashcardSet>>() {}.type,
            dataStore = androidContext().traditionalChoiceDataStore,
            preferenceKey = stringPreferencesKey("traditional_flashcard_sets")
        )
    }

    // Named Storage for MultipleChoiceFlashcardSet
    single<Storage<MultipleChoiceFlashcardSet>>(named("multipleChoiceStorage")) {
        MultipleChoiceFlashcardPersistentStorage(
            gson = get(),
            type = object : TypeToken<List<MultipleChoiceFlashcardSet>>() {}.type,
            dataStore = androidContext().multipleChoiceDataStore,
            preferenceKey = stringPreferencesKey("multiple_choice_flashcard_sets")
        )
    }

    single { Gson() }

    viewModel {
        CreateTraditionalFlashcardViewModel(
            traditionalFlashcardStorage = get(named("traditionalStorage"))
        )
    }
    viewModel {
        CreateMultipleChoiceFlashcardViewModel(
            multipleChoiceFlashcardStorage = get(named("multipleChoiceStorage"))
        )
    }
    viewModel {
        ViewFlashcardSetsViewModel(
            traditionalFlashcardStorage = get(named("traditionalStorage")),
            multipleChoiceFlashcardStorage = get(named("multipleChoiceStorage"))
        )
    }
    viewModel {
        PlayFlashcardSetsViewModel(
            traditionalFlashcardStorage = get(named("traditionalStorage")),
            multipleChoiceFlashcardStorage = get(named("multipleChoiceStorage"))
        )
    }
    viewModel {
        ViewTraditionalFlashcardViewModel(
            traditionalFlashcardStorage = get(named("traditionalStorage"))
        )
    }
    viewModel {
        ViewMultipleChoiceFlashcardViewModel(
            multipleChoiceFlashcardPersistentStorage = get(named("multipleChoiceStorage"))
        )
    }
    viewModel {
        PlayTraditionalFlashcardViewModel(
            traditionalFlashcardStorage = get(named("traditionalStorage"))
        )
    }
    viewModel {
        PlayMultipleChoiceFlashcardViewModel(
            multipleChoiceFlashcardStorage = get(named("multipleChoiceStorage"))
        )
    }
    viewModel {
        MultipleChoiceResultsViewModel(
            multipleChoiceFlashcardStorage = get(named("multipleChoiceStorage"))
        )
    }
    viewModel {
        TraditionalResultsViewModel(
            traditionalFlashcardStorage = get(named("traditionalStorage"))
        )
    }
    viewModel {
        EditTraditionalFlashcardViewModel(
            traditionalFlashcardStorage = get(named("traditionalStorage"))
        )
    }
    viewModel {
        EditMultipleChoiceFlashcardViewModel(
            multipleChoiceFlashcardStorage = get(named("multipleChoiceStorage"))
        )
    }

}
