package co.kr.data.network.model

import co.kr.tnt.domain.model.UserType
import kotlinx.serialization.Serializable

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
    val birthday: String? = null,
    val height: Double? = null,
    val weight: Double? = null,
    val goalContents: List<String>? = null,
    val cautionNote: String? = "",
)

fun UserType.toSignUpRequest(
    socialId: String,
    socialType: String,
    email: String,
    fcmToken: String,
): SignUpRequest {
    return when (this) {
        is UserType.Trainer -> SignUpRequest(
            memberType = "trainer",
            name = name,
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
            name = name,
            birthday = birthday?.toString(),
            height = height.toDouble(),
            weight = weight,
            goalContents = ptPurpose,
            cautionNote = caution?.ifBlank { null },
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
