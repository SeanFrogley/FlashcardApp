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
) : Parcelable {
    companion object {
        /**
         * Gets a list of basic (pre-defined) multiple-choice flashcard sets
         */
        fun getPrepopulatedMultipleChoiceFlashcardSets(): List<MultipleChoiceFlashcardSet> {
            return listOf(
                MultipleChoiceFlashcardSet(
                    id = 1,
                    title = "Programming Basics",
                    flashcards = listOf(
                        MultipleChoiceFlashcard(
                            id = 1,
                            question = "What does OOP stand for?",
                            options = listOf(
                                MultipleChoiceOption(text = "Object-Oriented Programming", isCorrect = true),
                                MultipleChoiceOption(text = "Object-Oriented Process", isCorrect = false),
                                MultipleChoiceOption(text = "Order of Processing", isCorrect = false),
                                MultipleChoiceOption(text = "Operational Output Processing", isCorrect = false)
                            )
                        ),
                        MultipleChoiceFlashcard(
                            id = 2,
                            question = "Which of the following is not a programming language?",
                            options = listOf(
                                MultipleChoiceOption(text = "Java", isCorrect = false),
                                MultipleChoiceOption(text = "Python", isCorrect = false),
                                MultipleChoiceOption(text = "Kotlin", isCorrect = false),
                                MultipleChoiceOption(text = "HTML", isCorrect = true)
                            )
                        )
                    )
                ),
                MultipleChoiceFlashcardSet(
                    id = 2,
                    title = "Advanced Topics",
                    flashcards = listOf(
                        MultipleChoiceFlashcard(
                            id = 3,
                            question = "Which keyword is used to inherit a class in Kotlin?",
                            options = listOf(
                                MultipleChoiceOption(text = "extends", isCorrect = false),
                                MultipleChoiceOption(text = "implements", isCorrect = false),
                                MultipleChoiceOption(text = "inherits", isCorrect = false),
                                MultipleChoiceOption(text = "open", isCorrect = true)
                            )
                        ),
                        MultipleChoiceFlashcard(
                            id = 4,
                            question = "What is the purpose of a lambda expression?",
                            options = listOf(
                                MultipleChoiceOption(text = "To create an anonymous function", isCorrect = true),
                                MultipleChoiceOption(text = "To define a new class", isCorrect = false),
                                MultipleChoiceOption(text = "To define a constant", isCorrect = false),
                                MultipleChoiceOption(text = "To create a loop", isCorrect = false)
                            )
                        )
                    )
                )
            )
        }
    }
}