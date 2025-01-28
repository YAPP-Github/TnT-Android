package co.kr.tnt.domain.model

import java.time.LocalDate

sealed class UserType {
    abstract val id: String
    abstract val name: String
    abstract val image: String?

    data class Trainer(
        override val id: String = "",
        override val name: String = "",
        override val image: String? = null,
    ) : UserType()

    data class Trainee(
        override val id: String = "",
        override val name: String = "",
        override val image: String? = null,
        val birthday: LocalDate? = null,
        val age: Int? = 0,
        val weight: Double = 0.0,
        val height: Int = 0,
        val ptPurpose: List<String> = emptyList(),
        val caution: String? = "",
    ) : UserType()
}
