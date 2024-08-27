package nz.ac.canterbury.seng303.myflashcardapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.FlowPreview
import nz.ac.canterbury.seng303.myflashcardapp.models.TraditionalFlashcardSet
import nz.ac.canterbury.seng303.myflashcardapp.viewmodels.CreateMultipleChoiceFlashcardViewModel
import nz.ac.canterbury.seng303.myflashcardapp.viewmodels.CreateTraditionalFlashcardViewModel
import nz.ac.canterbury.seng303.myflashcardapp.viewmodels.ViewFlashcardSetsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "traditional_flashcard_data_sets")

@FlowPreview
val dataAccessModule = module {
    single<Storage<TraditionalFlashcardSet>> {
        PersistentStorage(
            gson = get(),
            type = object : TypeToken<List<TraditionalFlashcardSet>>() {}.type,
            preferenceKey = stringPreferencesKey("notes"),
            dataStore = androidContext().dataStore
        )
    }

    single { Gson() }

    viewModel {
        CreateTraditionalFlashcardViewModel (
            traditionalFlashcardStorage = get()
        )
    }

    viewModel {
        CreateMultipleChoiceFlashcardViewModel (
            multipleChoiceFlashcardStorage = get()
        )
    }

    viewModel {
        ViewFlashcardSetsViewModel(
            traditionalFlashcardStorage = get()
        )
    }
}