package co.kr.tnt.signup.common.role.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.domain.model.UserType

sealed class RoleState(
    @StringRes val textResId: Int,
    @DrawableRes val imageResId: Int,
) {
    data object Trainer : RoleState(
        textResId = R.string.trainer,
        imageResId = R.drawable.img_select_role_trainer,
    )

    data object Trainee : RoleState(
        textResId = R.string.trainee,
        imageResId = R.drawable.img_select_role_trainee,
    )

    companion object {
        fun fromDomain(role: UserType): RoleState {
            return when (role) {
                UserType.Trainer -> Trainer
                UserType.Trainee -> Trainee
            }
        }
    }
}
