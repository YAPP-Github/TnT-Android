package co.kr.tnt.signup.trainee.model

import androidx.annotation.StringRes
import co.kr.tnt.core.designsystem.R

enum class PTPurpose(
    @StringRes val textResId: Int,
) {
    LOSS_WEIGHT(R.string.signup_pt_purpose_loss_weight),
    STRENGTH(R.string.signup_pt_purpose_strength),
    HEALTH_CARE(R.string.signup_pt_purpose_health_care),
    FLEXIBILITY(R.string.signup_pt_purpose_flexibility),
    BODY_PROFILE(R.string.signup_pt_purpose_body_profile),
    POSTURE_CORRECTION(R.string.signup_pt_purpose_posture_correction),
}
