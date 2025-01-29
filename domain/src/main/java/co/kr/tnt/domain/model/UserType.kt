package co.kr.tnt.domain.model

import java.time.LocalDate

sealed class UserType {
    abstract val id: String
    abstract val name: String
    abstract val image: String?

    data class Trainer(
        override val id: String,
        override val name: String,
        override val image: String?,
    ) : UserType()

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
    ) : UserType()
}
