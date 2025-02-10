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
        val weight: Double,
        val height: Int,
        val ptPurpose: List<String>,
        val caution: String?,
        val isConnected: Boolean,
    ) : User() {
        /** 한국식 나이 */
        val age: Int? =
            if (birthday == null) {
                null
            } else {
                LocalDate.now().year - birthday.year + 1
            }

        companion object {
            val EMPTY = Trainee(
                id = "",
                name = "",
                image = null,
                birthday = null,
                weight = 0.0,
                height = 0,
                ptPurpose = emptyList(),
                caution = null,
                isConnected = false,
            )
        }
    }
}
