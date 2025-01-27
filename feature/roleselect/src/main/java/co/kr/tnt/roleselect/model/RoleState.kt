package co.kr.tnt.roleselect.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import co.kr.tnt.domain.model.UserType
import co.kr.tnt.feature.roleselect.R
import co.kr.tnt.core.ui.R as uiResource

sealed class RoleState(
    @StringRes val textResId: Int,
    @DrawableRes val imageResId: Int,
) {
    data object Trainer : RoleState(
        textResId = uiResource.string.trainer,
        imageResId = R.drawable.img_select_role_trainer,
    )

    data object Trainee : RoleState(
        textResId = uiResource.string.trainee,
        imageResId = R.drawable.img_select_role_trainee,
    )

    companion object {
        fun fromDomain(role: UserType): RoleState {
            return when (role) {
                is UserType.Trainer -> Trainer
                is UserType.Trainee -> Trainee
            }
        }
    }
}
