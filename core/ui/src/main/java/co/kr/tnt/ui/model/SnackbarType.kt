package co.kr.tnt.ui.model

import androidx.annotation.DrawableRes
import co.kr.tnt.core.designsystem.R

enum class SnackbarType(
    @DrawableRes val iconRes: Int,
) {
    WARNING(R.drawable.ic_warning),
    SUCCESS(R.drawable.ic_check_success),
}
