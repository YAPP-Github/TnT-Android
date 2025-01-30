package co.kr.tnt.trainee.mypage.model

import androidx.annotation.StringRes
import co.kr.tnt.feature.trainee.mypage.R

enum class PopupType(
    @StringRes val firstPopupTitle: Int,
    @StringRes val firstPopupContent: Int,
    @StringRes val secondPopupTitle: Int,
    @StringRes val secondPopupContent: Int,
) {
    LOGOUT(
        firstPopupTitle = R.string.logout_title,
        firstPopupContent = R.string.logout_content,
        secondPopupTitle = R.string.logout_complete_title,
        secondPopupContent = R.string.logout_content,
    ),
    DELETE_ACCOUNT(
        firstPopupTitle = R.string.delete_account_title,
        firstPopupContent = R.string.delete_account_content,
        secondPopupTitle = R.string.delete_account_complete_title,
        secondPopupContent = R.string.delete_account_complete_content,
    ),
    DISCONNECT(
        firstPopupTitle = R.string.disconnect_title,
        firstPopupContent = R.string.disconnect_content,
        secondPopupTitle = R.string.disconnect_complete_title,
        secondPopupContent = R.string.disconnect_complete_content,
    ),
}
