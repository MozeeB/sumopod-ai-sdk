package com.cikup.sumopod.ai

import com.cikup.sumopod.ai.error.SumoPodException
import com.cikup.sumopod.ai.internal.HttpClientFactory
import com.cikup.sumopod.ai.model.ChatCompletionRequest
import com.cikup.sumopod.ai.model.ChatMessage
import com.cikup.sumopod.ai.model.ChatRole
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class AndroidClientTest {

    private val testConfig = SumoPodConfig(
        apiKey = "sk-testKeyForAndroidTesting123",
        logLevel = SumoPodConfig.LogLevel.NONE,
    )

    private val chatSuccessResponse = """
    {
        "id": "chatcmpl-android",
        "object": "chat.completion",
        "created": 1700000000,
        "model": "gpt-4o-mini",
        "choices": [{
            "index": 0,
            "message": {"role": "assistant", "content": "Hello Android!"},
            "finish_reason": "stop"
        }],
        "usage": {"prompt_tokens": 8, "completion_tokens": 3, "total_tokens": 11}
    }
    """.trimIndent()

    private fun createClient(engine: MockEngine): SumoPodAIClient {
        val httpClient = HttpClientFactory.create(testConfig, engine)
        return SumoPodAIClient(testConfig, httpClient)
    }

    @Test
    fun chatCompletionSuccessOnAndroid() = runTest {
        val engine = MockEngine { _ ->
            respond(
                content = chatSuccessResponse,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
        val client = createClient(engine)
        val response = client.chatCompletion(
            ChatCompletionRequest(
                model = "gpt-4o-mini",
                messages = listOf(ChatMessage(ChatRole.User, "Hi")),
            )
        )
        assertEquals("Hello Android!", response.choices.first().message.content)
        assertEquals(11, response.usage?.totalTokens)
    }

    @Test
    fun authorizationHeaderSentOnAndroid() = runTest {
        var capturedAuth: String? = null
        val engine = MockEngine { request ->
            capturedAuth = request.headers[HttpHeaders.Authorization]
            respond(
                content = chatSuccessResponse,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
        val client = createClient(engine)
        client.chatCompletion(
            ChatCompletionRequest(
                model = "gpt-4o-mini",
                messages = listOf(ChatMessage(ChatRole.User, "Hi")),
            )
        )
        assertTrue(capturedAuth?.startsWith("Bearer sk-") == true)
    }

    @Test
    fun userAgentHeaderSentOnAndroid() = runTest {
        var capturedUserAgent: String? = null
        val engine = MockEngine { request ->
            capturedUserAgent = request.headers["User-Agent"]
            respond(
                content = chatSuccessResponse,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
        val client = createClient(engine)
        client.chatCompletion(
            ChatCompletionRequest(
                model = "gpt-4o-mini",
                messages = listOf(ChatMessage(ChatRole.User, "Hi")),
            )
        )
        assertEquals("SumoPod-AI-SDK/0.1.0 (KMP)", capturedUserAgent)
    }

    @Test
    fun error401ThrowsAuthExceptionOnAndroid() = runTest {
        val engine = MockEngine { _ ->
            respond(
                content = """{"error":{"message":"Invalid key"}}""",
                status = HttpStatusCode.Unauthorized,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
        val client = createClient(engine)
        assertFailsWith<SumoPodException.AuthenticationException> {
            client.chatCompletion(
                ChatCompletionRequest(
                    model = "gpt-4o-mini",
                    messages = listOf(ChatMessage(ChatRole.User, "Hi")),
                )
            )
        }
    }

    @Test
    fun error429ThrowsRateLimitExceptionOnAndroid() = runTest {
        val engine = MockEngine { _ ->
            respond(
                content = """{"error":{"message":"Rate limited"}}""",
                status = HttpStatusCode.TooManyRequests,
                headers = headersOf(
                    HttpHeaders.ContentType to listOf("application/json"),
                    "Retry-After" to listOf("60"),
                ),
            )
        }
        val client = createClient(engine)
        val exception = assertFailsWith<SumoPodException.RateLimitException> {
            client.chatCompletion(
                ChatCompletionRequest(
                    model = "gpt-4o-mini",
                    messages = listOf(ChatMessage(ChatRole.User, "Hi")),
                )
            )
        }
        assertEquals(60_000L, exception.retryAfterMs)
    }

    @Test
    fun error400ThrowsInvalidRequestExceptionOnAndroid() = runTest {
        val engine = MockEngine { _ ->
            respond(
                content = """{"error":{"message":"Invalid model","param":"model"}}""",
                status = HttpStatusCode.BadRequest,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
        val client = createClient(engine)
        val exception = assertFailsWith<SumoPodException.InvalidRequestException> {
            client.chatCompletion(
                ChatCompletionRequest(
                    model = "gpt-4o-mini",
                    messages = listOf(ChatMessage(ChatRole.User, "Hi")),
                )
            )
        }
        assertEquals("model", exception.param)
    }
}
