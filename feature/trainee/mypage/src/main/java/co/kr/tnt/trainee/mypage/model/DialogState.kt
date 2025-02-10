package co.kr.tnt.trainee.mypage.model

import androidx.annotation.StringRes
import co.kr.tnt.feature.trainee.mypage.R
import co.kr.tnt.core.ui.R as coreR

enum class DialogState(
    @StringRes val warningDialogTitle: Int,
    @StringRes val warningDialogContent: Int,
    @StringRes val completeDialogTitle: Int,
    @StringRes val completeDialogContent: Int,
) {
    LOGOUT(
        warningDialogTitle = coreR.string.logout_title,
        warningDialogContent = coreR.string.logout_content,
        completeDialogTitle = coreR.string.logout_complete_title,
        completeDialogContent = coreR.string.logout_content,
    ),
    DELETE_ACCOUNT(
        warningDialogTitle = R.string.delete_account_title,
        warningDialogContent = R.string.delete_account_content,
        completeDialogTitle = R.string.delete_account_complete_title,
        completeDialogContent = R.string.delete_account_complete_content,
    ),
    DISCONNECT(
        warningDialogTitle = R.string.disconnect_title,
        warningDialogContent = R.string.disconnect_content,
        completeDialogTitle = R.string.disconnect_complete_title,
        completeDialogContent = R.string.disconnect_complete_content,
    ),
}
