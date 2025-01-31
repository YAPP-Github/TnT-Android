package co.kr.tnt.trainee.mypage.model

import androidx.annotation.StringRes
import co.kr.tnt.feature.trainee.mypage.R

enum class DialogState(
    @StringRes val warningDialogTitle: Int,
    @StringRes val warningDialogContent: Int,
    @StringRes val completeDialogTitle: Int,
    @StringRes val completeDialogContent: Int,
) {
    LOGOUT(
        warningDialogTitle = R.string.logout_title,
        warningDialogContent = R.string.logout_content,
        completeDialogTitle = R.string.logout_complete_title,
        completeDialogContent = R.string.logout_content,
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
