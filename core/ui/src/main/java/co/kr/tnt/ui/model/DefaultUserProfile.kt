package co.kr.tnt.ui.model

import androidx.annotation.DrawableRes
import co.kr.tnt.core.ui.R
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
        fun fromDomain(type: UserType): DefaultUserProfile {
            return when (type) {
                is UserType.Trainee -> Trainer
                is UserType.Trainer -> Trainee
            }
        }
    }
}
