package co.kr.tnt.ui.model

import androidx.annotation.DrawableRes
import co.kr.tnt.core.ui.R
import co.kr.tnt.domain.model.User
import co.kr.tnt.domain.model.UserType

sealed class DefaultUserProfile(
    @DrawableRes val image: Int,
) {
    data object Trainer : DefaultUserProfile(
        image = R.drawable.img_default_profile_trainer,
    )

    data object Trainee : DefaultUserProfile(
        image = R.drawable.img_default_profile_trainee,
    )

    companion object {
        fun fromDomain(type: User): DefaultUserProfile {
            return when (type) {
                is User.Trainee -> Trainer
                is User.Trainer -> Trainee
            }
        }

        fun fromDomain(type: UserType): DefaultUserProfile {
            return when (type) {
                UserType.TRAINER -> Trainer
                UserType.TRAINEE -> Trainee
            }
        }
    }
}
