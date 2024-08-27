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

    companion object {
        /**
         * Gets a list of basic (pre-defined) flashcard sets
         */
        fun getPrepopulatedTraditionalFlashcardSets(): List<TraditionalFlashcardSet> {
            return listOf(
                TraditionalFlashcardSet(
                    id = 1,
                    title = "Programming Concepts",
                    flashcards = listOf(
                        TraditionalFlashcard(
                            id = 1,
                            term = "OOP",
                            definition = "Object-Oriented Programming, a programming paradigm based on the concept of 'objects'."
                        ),
                        TraditionalFlashcard(
                            id = 2,
                            term = "Encapsulation",
                            definition = "The practice of keeping fields within a class private, then providing access via public methods."
                        ),
                        TraditionalFlashcard(
                            id = 3,
                            term = "Inheritance",
                            definition = "A mechanism where a new class is derived from an existing class."
                        ),
                        TraditionalFlashcard(
                            id = 4,
                            term = "Polymorphism",
                            definition = "The ability of different classes to be treated as instances of the same class through inheritance."
                        ),
                        TraditionalFlashcard(
                            id = 5,
                            term = "Abstraction",
                            definition = "The concept of hiding the complex implementation details and showing only the essential features of the object."
                        )
                    )
                ),
                TraditionalFlashcardSet(
                    id = 2,
                    title = "Software Engineering Principles",
                    flashcards = listOf(
                        TraditionalFlashcard(
                            id = 6,
                            term = "Agile",
                            definition = "A methodology focused on iterative development and collaboration between teams."
                        ),
                        TraditionalFlashcard(
                            id = 7,
                            term = "Scrum",
                            definition = "A framework for managing work with an emphasis on teamwork, accountability, and iterative progress."
                        ),
                        TraditionalFlashcard(
                            id = 8,
                            term = "Waterfall",
                            definition = "A linear project management approach where each phase depends on the deliverables of the previous one."
                        ),
                        TraditionalFlashcard(
                            id = 9,
                            term = "DevOps",
                            definition = "A set of practices that combines software development (Dev) and IT operations (Ops)."
                        ),
                        TraditionalFlashcard(
                            id = 10,
                            term = "CI/CD",
                            definition = "Continuous Integration and Continuous Delivery/Deployment; practices in software engineering."
                        )
                    )
                )
            )
        }
    }
}
