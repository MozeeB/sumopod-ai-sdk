package com.cikup.sumopod.ai.error

public sealed class SumopodException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    public class AuthenticationException : SumopodException("Invalid or missing API key")
    public class RateLimitException(public val retryAfterMs: Long? = null) : SumopodException("Rate limit exceeded")
    public class InvalidRequestException(public val param: String? = null) : SumopodException(
        if (param != null) "Invalid request parameter: $param" else "Invalid request"
    )
    public class ApiException(public val statusCode: Int) : SumopodException("API error: $statusCode")
    public class NetworkException(cause: Throwable? = null) : SumopodException("Network error", cause)
    public class StreamException(cause: Throwable? = null) : SumopodException("Stream interrupted", cause)
}
