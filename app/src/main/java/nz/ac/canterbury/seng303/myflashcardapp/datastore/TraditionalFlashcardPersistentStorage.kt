package nz.ac.canterbury.seng303.myflashcardapp.datastore

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import nz.ac.canterbury.seng303.myflashcardapp.models.Identifiable
import nz.ac.canterbury.seng303.myflashcardapp.models.TraditionalFlashcardSet
import java.lang.reflect.Type


class TraditionalFlashcardPersistentStorage<T>(
    private val gson: Gson,
    private val type: Type,
    private val dataStore: DataStore<Preferences>,
    private val preferenceKey: Preferences.Key<String>
) : Storage<T> where T : Identifiable {

    override fun insert(data: T): Flow<Int> = flow {
        val cachedDataClone = getAll().first().toMutableList()
        cachedDataClone.add(data)
        dataStore.edit { preferences ->
            val jsonString = gson.toJson(cachedDataClone, type)
            preferences[preferenceKey] = jsonString
            emit(OPERATION_SUCCESS)
        }
    }

    override fun insertAll(data: List<T>): Flow<Int> = flow {
        val cachedDataClone = getAll().first().toMutableList()
        cachedDataClone.addAll(data)
        dataStore.edit {
            val jsonString = gson.toJson(cachedDataClone, type)
            it[preferenceKey] = jsonString
            emit(OPERATION_SUCCESS)
        }
    }

    override fun getAll(): Flow<List<T>> = dataStore.data.map { preferences ->
        val jsonString = preferences[preferenceKey] ?: EMPTY_JSON_STRING
        gson.fromJson(jsonString, type)
    }

    override fun edit(identifier: Int, data: T): Flow<Int> = flow {
        val cachedDataClone = getAll().first().toMutableList()
        val index = cachedDataClone.indexOfFirst { it.getIdentifier() == identifier }
        if (index != -1) {
            cachedDataClone[index] = data
            dataStore.edit {
                val jsonString = gson.toJson(cachedDataClone, type)
                it[preferenceKey] = jsonString
                emit(OPERATION_SUCCESS)
            }
        } else {
            emit(OPERATION_FAILURE)
        }
    }

    override fun get(where: (T) -> Boolean): Flow<T> = flow {
        val data = getAll().first().first(where)
        emit(data)
    }

    override fun delete(identifier: Int): Flow<Int> = flow {
        val cachedDataClone = getAll().first().toMutableList()
        val updatedData = cachedDataClone.filterNot { it.getIdentifier() == identifier }
        dataStore.edit {
            val jsonString = gson.toJson(updatedData, type)
            it[preferenceKey] = jsonString
            emit(OPERATION_SUCCESS)
        }
    }

    override fun deleteFlashcardFromSet(setId: Int, flashcardIndex: Int): Flow<Int> = flow {
        val cachedDataClone = getAll().first().toMutableList()
        val setIndex = cachedDataClone.indexOfFirst { it.getIdentifier() == setId }
        if (setIndex != -1) {
            val set = cachedDataClone[setIndex] as TraditionalFlashcardSet // assuming the type
            val updatedFlashcards = set.flashcards.toMutableList().apply {
                removeAt(flashcardIndex)
            }

            if (updatedFlashcards.isEmpty()) {
                // If no flashcards are left, delete the entire set
                cachedDataClone.removeAt(setIndex)
            } else {
                // Otherwise, update the set with the remaining flashcards
                val updatedSet = set.copy(flashcards = updatedFlashcards)
                cachedDataClone[setIndex] = updatedSet as T
            }

            dataStore.edit {
                val jsonString = gson.toJson(cachedDataClone, type)
                it[preferenceKey] = jsonString
                emit(OPERATION_SUCCESS)
            }
        } else {
            emit(OPERATION_FAILURE)
        }
    }


    companion object {
        const val OPERATION_SUCCESS = 1
        const val OPERATION_FAILURE = -1
        private const val EMPTY_JSON_STRING = "[]"
    }

    fun clear(): Flow<Unit> = flow {
        dataStore.edit { preferences ->
            preferences.clear() // This clears all data in the DataStore
        }
        emit(Unit)
    }
}
