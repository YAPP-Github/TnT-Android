package co.kr.tnt.login.model

import androidx.annotation.StringRes
import co.kr.tnt.domain.model.Term
import co.kr.tnt.domain.utils.AppUrls
import co.kr.tnt.feature.login.R

sealed class TermState(
    @StringRes val titleRes: Int,
    val link: String,
) {
    abstract val isRequired: Boolean

    data class TermsOfServiceState(
        override val isRequired: Boolean,
    ) : TermState(
            titleRes = R.string.terms_of_service,
            link = AppUrls.TERMS_OF_SERVICE_URL,
        )

    data class PrivacyPolicyState(
        override val isRequired: Boolean,
    ) : TermState(
            titleRes = R.string.privacy_policy,
            link = AppUrls.PRIVACY_POLICY_URL,
        )

    companion object {
        fun fromDomain(term: Term): TermState {
            return when (term) {
                Term.TERMS_OF_SERVICE -> TermsOfServiceState(term.isRequired)
                Term.PRIVACY_POLICY -> PrivacyPolicyState(term.isRequired)
            }
        }
    }
}
