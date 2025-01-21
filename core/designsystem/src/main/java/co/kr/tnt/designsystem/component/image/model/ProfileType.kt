package co.kr.tnt.designsystem.component.image.model

import co.kr.tnt.core.designsystem.R

enum class ProfileType(
    val defaultImage: Int,
) {
    Trainer(
        defaultImage = R.drawable.img_default_profile_trainer,
    ),
    Trainee(
        defaultImage = R.drawable.img_default_profile_trainee,
    ),
}
