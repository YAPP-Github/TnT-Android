package co.kr.data.network.model.exception

sealed class NetworkException(override val message: String) : Exception(message) {
    data class BadRequestException(
        override val message: String = "Bad request",
    ) : NetworkException(message)

    data class UnauthorizedException(
        override val message: String = "Unauthorized",
    ) : NetworkException(message)

    data class ForbiddenException(
        override val message: String = "Forbidden",
    ) : NetworkException(message)

    data class NotFoundException(
        override val message: String = "Not Found",
    ) : NetworkException(message)

    data class ConflictException(
        override val message: String = "Conflict",
    ) : NetworkException(message)

    data class ServerException(
        override val message: String = "Server Error",
    ) : NetworkException(message)

    data class UnknownException(
        override val message: String = "Unknown Error",
    ) : NetworkException(message)
}
