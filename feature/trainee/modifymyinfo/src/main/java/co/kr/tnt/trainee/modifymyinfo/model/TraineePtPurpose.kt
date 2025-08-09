package co.kr.tnt.trainee.modifymyinfo.model

import androidx.annotation.StringRes
import co.kr.tnt.core.ui.R.string.core_body_profile
import co.kr.tnt.core.ui.R.string.core_flexibility
import co.kr.tnt.core.ui.R.string.core_health_care
import co.kr.tnt.core.ui.R.string.core_loss_weight
import co.kr.tnt.core.ui.R.string.core_posture_correction
import co.kr.tnt.core.ui.R.string.core_strength_improvement
import co.kr.tnt.domain.model.PtPurpose

enum class TraineePtPurpose(
    @StringRes val textResId: Int,
) {
    LOSS_WEIGHT(core_loss_weight),
    STRENGTH(core_strength_improvement),
    HEALTH_CARE(core_health_care),
    FLEXIBILITY(core_flexibility),
    BODY_PROFILE(core_body_profile),
    POSTURE_CORRECTION(core_posture_correction),
    ;

    companion object {
        fun from(purpose: PtPurpose): TraineePtPurpose {
            return when (purpose) {
                PtPurpose.LOSS_WEIGHT -> LOSS_WEIGHT
                PtPurpose.STRENGTH -> STRENGTH
                PtPurpose.HEALTH_CARE -> HEALTH_CARE
                PtPurpose.FLEXIBILITY -> FLEXIBILITY
                PtPurpose.BODY_PROFILE -> BODY_PROFILE
                PtPurpose.POSTURE_CORRECTION -> POSTURE_CORRECTION
            }
        }
    }
}
