package co.kr.tnt.domain.repository

import co.kr.tnt.domain.model.SignUpResult
import co.kr.tnt.domain.model.User
import java.io.File

interface SignUpRepository {
    suspend fun signUp(
        profileImage: File?,
        user: User,
        socialId: String,
        socialType: String,
        email: String,
    ): SignUpResult
}
