package com.cikup.sumopod.ai.error

public sealed class SumoPodException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    public class AuthenticationException : SumoPodException("Invalid or missing API key")
    public class RateLimitException(public val retryAfterMs: Long? = null) : SumoPodException("Rate limit exceeded")
    public class InvalidRequestException(public val param: String? = null) : SumoPodException(
        if (param != null) "Invalid request parameter: $param" else "Invalid request"
    )
    public class ApiException(public val statusCode: Int) : SumoPodException("API error: $statusCode")
    public class NetworkException(cause: Throwable? = null) : SumoPodException("Network error", cause)
    public class StreamException(cause: Throwable? = null) : SumoPodException("Stream interrupted", cause)
}
