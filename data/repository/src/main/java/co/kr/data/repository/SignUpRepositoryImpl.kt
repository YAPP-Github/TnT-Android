package co.kr.data.repository

import co.kr.data.network.model.SignUpRequest
import co.kr.data.network.model.toDomain
import co.kr.data.network.model.toSignUpRequest
import co.kr.data.network.source.SignUpRemoteDataSource
import co.kr.data.storage.source.SessionLocalDataSource
import co.kr.tnt.domain.model.SignUpResult
import co.kr.tnt.domain.model.User
import co.kr.tnt.domain.repository.SignUpRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SignUpRepositoryImpl @Inject constructor(
    private val signupRemoteDataSource: SignUpRemoteDataSource,
    private val sessionLocalDataSource: SessionLocalDataSource,
    private val json: Json,
) : SignUpRepository {
    override suspend fun signUp(
        profileImage: File?,
        user: User,
        socialId: String,
        socialType: String,
        email: String,
    ): SignUpResult {
        val profileImagePart = profileImage?.let {
            val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("profileImage", it.name, requestFile)
        }

        // TODO FCM token
        val signUpRequest = user.toSignUpRequest(
            socialId = socialId,
            socialType = socialType,
            email = email,
            fcmToken = "EMPTY",
        )
        val requestBody = signUpRequest.toRequestBody()

        val response = signupRemoteDataSource.postSignUp(
            profileImage = profileImagePart,
            request = requestBody,
        )

        sessionLocalDataSource.updateSessionId(response.sessionId)

        return response.toDomain()
    }

    private fun SignUpRequest.toRequestBody(): RequestBody {
        val jsonString = json.encodeToString(this)
        return jsonString.toRequestBody("application/json".toMediaTypeOrNull())
    }
}
