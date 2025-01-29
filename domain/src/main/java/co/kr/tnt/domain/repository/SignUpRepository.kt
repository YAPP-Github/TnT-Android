package co.kr.tnt.domain.repository

import co.kr.tnt.domain.model.SignUpResult
import co.kr.tnt.domain.model.UserType
import java.io.File

interface SignUpRepository {
    suspend fun signUp(
        profileImage: File?,
        userType: UserType,
        socialId: String,
        socialType: String,
        email: String,
    ): SignUpResult
}
