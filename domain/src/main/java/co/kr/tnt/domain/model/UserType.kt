package co.kr.tnt.domain.model

sealed class UserType {
    abstract val id: String
    abstract val name: String
    abstract val image: String?

    data class Trainer(
        override val id: String,
        override val name: String,
        override val image: String? = null,
    ) : UserType()

    data class Trainee(
        override val id: String,
        override val name: String,
        override val image: String? = null,
        val age: Int,
        val weight: Float,
        val height: Int,
        val ptPurpose: List<String>,
        val caution: String? = null,
    ) : UserType()
}
