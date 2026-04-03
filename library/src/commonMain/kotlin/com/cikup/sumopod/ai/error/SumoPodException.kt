package com.cikup.sumopod.ai.error

sealed class SumoPodException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class AuthenticationException : SumoPodException("Invalid or missing API key")
    class RateLimitException(val retryAfterMs: Long? = null) : SumoPodException("Rate limit exceeded")
    class InvalidRequestException(val param: String? = null) : SumoPodException(
        if (param != null) "Invalid request parameter: $param" else "Invalid request"
    )
    class ApiException(val statusCode: Int) : SumoPodException("API error: $statusCode")
    class NetworkException(cause: Throwable? = null) : SumoPodException("Network error", cause)
    class StreamException(cause: Throwable? = null) : SumoPodException("Stream interrupted", cause)
}
