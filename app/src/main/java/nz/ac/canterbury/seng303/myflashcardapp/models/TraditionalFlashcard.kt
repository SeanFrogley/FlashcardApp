package nz.ac.canterbury.seng303.myflashcardapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents a Traditional Flashcard object with term and definition
 */

@Parcelize
data class TraditionalFlashcard(
    val id: Int,
    val term: String,
    val definition: String,
    var gotCorrect: Boolean? = null
) : Parcelable
