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

class ChatCompletionTest {

    private val testConfig = SumoPodConfig(
        apiKey = "sk-testKeyForUnitTesting12345",
        logLevel = SumoPodConfig.LogLevel.NONE,
    )

    private val successResponse = """
    {
        "id": "chatcmpl-test",
        "object": "chat.completion",
        "created": 1700000000,
        "model": "gpt-4o-mini",
        "choices": [{
            "index": 0,
            "message": {"role": "assistant", "content": "Hello from SumoPod!"},
            "finish_reason": "stop"
        }],
        "usage": {"prompt_tokens": 10, "completion_tokens": 5, "total_tokens": 15}
    }
    """.trimIndent()

    private fun createClient(engine: MockEngine): SumoPodAIClient {
        val httpClient = HttpClientFactory.create(testConfig, engine)
        return SumoPodAIClient(testConfig, httpClient)
    }

    @Test
    fun chatCompletionSuccess() = runTest {
        val engine = MockEngine { _ ->
            respond(
                content = successResponse,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
        val client = createClient(engine)
        val request = ChatCompletionRequest(
            model = "gpt-4o-mini",
            messages = listOf(ChatMessage(ChatRole.User, "Hi")),
        )
        val response = client.chatCompletion(request)
        assertEquals("Hello from SumoPod!", response.choices.first().message.content)
        assertEquals(15, response.usage?.totalTokens)
    }

    @Test
    fun chatCompletionSendsAuthHeader() = runTest {
        var capturedAuthHeader: String? = null
        val engine = MockEngine { request ->
            capturedAuthHeader = request.headers[HttpHeaders.Authorization]
            respond(
                content = successResponse,
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
        assertTrue(capturedAuthHeader?.startsWith("Bearer sk-") == true)
    }

    @Test
    fun chatCompletion401ThrowsAuthException() = runTest {
        val engine = MockEngine { _ ->
            respond(
                content = """{"error":{"message":"Invalid API key","type":"invalid_request_error"}}""",
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
    fun chatCompletion429ThrowsRateLimitException() = runTest {
        val engine = MockEngine { _ ->
            respond(
                content = """{"error":{"message":"Rate limit exceeded"}}""",
                status = HttpStatusCode.TooManyRequests,
                headers = headersOf(
                    HttpHeaders.ContentType to listOf("application/json"),
                    "Retry-After" to listOf("30"),
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
        assertEquals(30_000L, exception.retryAfterMs)
    }

    @Test
    fun chatCompletion500ThrowsApiException() = runTest {
        val engine = MockEngine { _ ->
            respond(
                content = """{"error":{"message":"Internal server error"}}""",
                status = HttpStatusCode.InternalServerError,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
        val client = createClient(engine)
        val exception = assertFailsWith<SumoPodException.ApiException> {
            client.chatCompletion(
                ChatCompletionRequest(
                    model = "gpt-4o-mini",
                    messages = listOf(ChatMessage(ChatRole.User, "Hi")),
                )
            )
        }
        assertEquals(500, exception.statusCode)
    }
}
