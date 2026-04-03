package com.cikup.sumopod.ai

import com.cikup.sumopod.ai.error.SumopodException
import kotlin.test.Test
import kotlin.test.assertFalse

class ErrorHandlingTest {

    @Test
    fun authExceptionDoesNotLeakKey() {
        val exception = SumopodException.AuthenticationException()
        assertFalse(exception.message!!.contains("sk-"))
    }

    @Test
    fun rateLimitExceptionDoesNotLeakDetails() {
        val exception = SumopodException.RateLimitException(retryAfterMs = 30_000)
        assertFalse(exception.message!!.contains("30000"))
    }

    @Test
    fun apiExceptionOnlyContainsStatusCode() {
        val exception = SumopodException.ApiException(statusCode = 503)
        assertFalse(exception.message!!.contains("internal"))
    }

    @Test
    fun networkExceptionDoesNotLeakUrl() {
        val exception = SumopodException.NetworkException()
        assertFalse(exception.message!!.contains("sumopod"))
    }
}
