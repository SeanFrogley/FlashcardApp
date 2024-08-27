package nz.ac.canterbury.seng303.myflashcardapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents a Multiple Choice Flashcard with a question and multiple options
 */
/**
 * Represents a set of Multiple Choice Flashcards
 */
@Parcelize
data class MultipleChoiceFlashcardSet(
    val id: Int,
    val title: String,
    val flashcards: List<MultipleChoiceFlashcard>
) : Parcelable, Identifiable {
    override fun getIdentifier(): Int = id
}