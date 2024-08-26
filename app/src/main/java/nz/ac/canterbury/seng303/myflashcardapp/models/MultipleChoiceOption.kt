package nz.ac.canterbury.seng303.myflashcardapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents an option in a Multiple Choice Flashcard
 */
@Parcelize
data class MultipleChoiceOption(
    val text: String,
    val isCorrect: Boolean
) : Parcelable