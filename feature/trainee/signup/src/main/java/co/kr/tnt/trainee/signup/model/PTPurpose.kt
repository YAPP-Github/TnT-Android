package co.kr.tnt.trainee.signup.model

import androidx.annotation.StringRes
import co.kr.tnt.feature.trainee.signup.R

enum class PTPurpose(
    @StringRes val textResId: Int,
) {
    LOSS_WEIGHT(R.string.loss_weight),
    STRENGTH(R.string.strength_improvement),
    HEALTH_CARE(R.string.health_care),
    FLEXIBILITY(R.string.flexibility),
    BODY_PROFILE(R.string.body_profile),
    POSTURE_CORRECTION(R.string.posture_correction),
}
