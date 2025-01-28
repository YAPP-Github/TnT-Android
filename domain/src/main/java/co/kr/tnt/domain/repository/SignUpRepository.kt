package co.kr.tnt.domain.repository

import co.kr.tnt.domain.model.SignUpResult
import co.kr.tnt.domain.model.UserType
import okhttp3.MultipartBody

interface SignUpRepository {
    suspend fun signUp(
        profileImage: MultipartBody.Part?,
        userType: UserType,
        socialId: String,
        socialType: String,
        email: String,
    ): SignUpResult
}
