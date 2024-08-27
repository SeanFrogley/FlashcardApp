package nz.ac.canterbury.seng303.myflashcardapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents a set of Traditional Flashcards
 */
@Parcelize
data class TraditionalFlashcardSet(
    val id: Int,
    val title: String,
    val flashcards: List<TraditionalFlashcard>
) : Parcelable, Identifiable {

    override fun getIdentifier() = id
}
