package co.kr.tnt.domain.model

import java.io.File

sealed interface ProfileImageUpdatePolicy {
    data object Keep : ProfileImageUpdatePolicy
    data class Change(val newProfileImage: File) : ProfileImageUpdatePolicy
    data object Remove : ProfileImageUpdatePolicy
}
