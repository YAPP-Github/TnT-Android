package co.kr.tnt.connect.model

interface UserProfile {
    val name: String
    val image: String?
}

data class TrainerProfile(
    override val name: String = "",
    override val image: String? = "",
) : UserProfile

data class TraineeProfile(
    override val name: String = "",
    override val image: String? = "",
    val age: Int = 0,
    val weight: Float = 0f,
    val height: Float = 0f,
    val ptPurpose: String = "",
    val caution: String = "",
) : UserProfile
