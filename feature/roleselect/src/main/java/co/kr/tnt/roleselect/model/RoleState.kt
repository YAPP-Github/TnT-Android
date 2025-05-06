package co.kr.tnt.roleselect.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import co.kr.tnt.core.ui.R.string.core_trainee
import co.kr.tnt.core.ui.R.string.core_trainer
import co.kr.tnt.domain.model.UserType
import co.kr.tnt.feature.roleselect.R

sealed class RoleState(
    @StringRes val textResId: Int,
    @DrawableRes val imageResId: Int,
) {
    data object Trainer : RoleState(
        textResId = core_trainer,
        imageResId = R.drawable.img_select_role_trainer,
    )

    data object Trainee : RoleState(
        textResId = core_trainee,
        imageResId = R.drawable.img_select_role_trainee,
    )

    companion object {
        fun fromDomain(type: UserType): RoleState {
            return when (type) {
                UserType.TRAINER -> Trainer
                UserType.TRAINEE -> Trainee
            }
        }
    }
}
