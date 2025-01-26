package co.kr.data.network.util

import co.kr.data.network.model.base.BaseErrorResponse
import co.kr.data.network.model.exception.NetworkException
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import retrofit2.Response

internal suspend inline fun <T> networkHandler(
    crossinline call: suspend () -> T,
): T = try {
    call()
} catch (httpException: HttpException) {
    val errorResponse = parseErrorResponse(httpException.response())
    val message = errorResponse.message

    throw when (httpException.code()) {
        400 -> NetworkException.BadRequestException(message)
        401 -> NetworkException.UnauthorizedException(message)
        403 -> NetworkException.ForbiddenException(message)
        404 -> NetworkException.NotFoundException(message)
        409 -> NetworkException.ConflictException(message)
        in 500 until 600 -> NetworkException.ServerException(message)
        else -> NetworkException.UnknownException(message)
    }
}

private fun parseErrorResponse(response: Response<*>?): BaseErrorResponse {
    return try {
        requireNotNull(
            response?.errorBody()?.string()?.let {
                Json.decodeFromString<BaseErrorResponse>(it)
            },
        )
    } catch (e: Exception) {
        throw NetworkException.UnknownException("Failed to parse error message,\n${e.message}")
    }
}
