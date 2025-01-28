package co.kr.data.network.model

import co.kr.tnt.domain.model.UserType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class SignUpRequest(
    val socialType: String,
    val socialId: String,
    val fcmToken: String,
    val serviceAgreement: Boolean,
    val collectionAgreement: Boolean,
    val advertisementAgreement: Boolean,
    val socialEmail: String,
    val memberType: String,
    val name: String,
    @Contextual
    val birthday: LocalDate? = null,
    val height: Double? = null,
    val weight: Double? = null,
    val goalContents: List<String>? = null,
    val cautionNote: String? = "",
)

object SignUpRequestMapper {
    fun fromUserType(
        userType: UserType,
        socialId: String,
        socialType: String,
        email: String,
        fcmToken: String,
    ): SignUpRequest {
        return when (userType) {
            is UserType.Trainer -> SignUpRequest(
                memberType = "trainer",
                name = userType.name,
                birthday = null,
                height = null,
                weight = null,
                goalContents = null,
                cautionNote = null,
                socialType = socialType,
                socialId = socialId,
                socialEmail = email,
                fcmToken = fcmToken,
                serviceAgreement = true,
                collectionAgreement = true,
                advertisementAgreement = true,
            )

            is UserType.Trainee -> SignUpRequest(
                memberType = "trainee",
                name = userType.name,
                birthday = userType.birthday,
                height = userType.height.toDouble(),
                weight = userType.weight,
                goalContents = userType.ptPurpose,
                cautionNote = userType.caution,
                socialType = socialType,
                socialId = socialId,
                socialEmail = email,
                fcmToken = fcmToken,
                serviceAgreement = true,
                collectionAgreement = true,
                advertisementAgreement = true,
            )
        }
    }
}
