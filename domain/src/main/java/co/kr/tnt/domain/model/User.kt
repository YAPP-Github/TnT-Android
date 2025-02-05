package co.kr.tnt.domain.model

import java.time.LocalDate

sealed class User {
    abstract val id: String
    abstract val name: String
    abstract val image: String?

    data class Trainer(
        override val id: String,
        override val name: String,
        override val image: String?,
    ) : User() {
        companion object {
            val EMPTY = Trainer(
                id = "",
                name = "",
                image = null,
            )
        }
    }

    data class Trainee(
        override val id: String,
        override val name: String,
        override val image: String?,
        val birthday: LocalDate?,
        val age: Int?,
        val weight: Double,
        val height: Int,
        val ptPurpose: List<String>,
        val caution: String?,
    ) : User() {
        companion object {
            val EMPTY = Trainee(
                id = "",
                name = "",
                image = null,
                birthday = null,
                age = 0,
                weight = 0.0,
                height = 0,
                ptPurpose = emptyList(),
                caution = null,
            )
        }
    }
}
