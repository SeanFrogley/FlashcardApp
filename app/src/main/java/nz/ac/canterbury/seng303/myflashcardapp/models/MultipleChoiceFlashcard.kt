package nz.ac.canterbury.seng303.myflashcardapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MultipleChoiceFlashcard(
    val id: Int,
    val question: String,
    val options: List<MultipleChoiceOption>,
    var gotCorrect: Boolean? = null
) : Parcelable
