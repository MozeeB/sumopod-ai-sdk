package com.cikup.sumopod.ai

import com.cikup.sumopod.ai.internal.HttpClientFactory
import com.cikup.sumopod.ai.model.ChatCompletionRequest
import com.cikup.sumopod.ai.model.ChatMessage
import com.cikup.sumopod.ai.model.ChatRole
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class StreamingTest {

    private val testConfig = SumoPodConfig(
        apiKey = "sk-testKeyForUnitTesting12345",
        logLevel = SumoPodConfig.LogLevel.NONE,
    )

    private val ssePayload = buildString {
        appendLine("data: {\"id\":\"chatcmpl-1\",\"object\":\"chat.completion.chunk\",\"created\":1700000000,\"model\":\"gpt-4o-mini\",\"choices\":[{\"index\":0,\"delta\":{\"role\":\"assistant\"},\"finish_reason\":null}]}")
        appendLine()
        appendLine("data: {\"id\":\"chatcmpl-1\",\"object\":\"chat.completion.chunk\",\"created\":1700000000,\"model\":\"gpt-4o-mini\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"Hello\"},\"finish_reason\":null}]}")
        appendLine()
        appendLine("data: {\"id\":\"chatcmpl-1\",\"object\":\"chat.completion.chunk\",\"created\":1700000000,\"model\":\"gpt-4o-mini\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\" World\"},\"finish_reason\":null}]}")
        appendLine()
        appendLine("data: {\"id\":\"chatcmpl-1\",\"object\":\"chat.completion.chunk\",\"created\":1700000000,\"model\":\"gpt-4o-mini\",\"choices\":[{\"index\":0,\"delta\":{},\"finish_reason\":\"stop\"}]}")
        appendLine()
        appendLine("data: [DONE]")
        appendLine()
    }

    @Test
    fun streamingCollectsAllChunks() = runTest {
        val engine = MockEngine { _ ->
            respond(
                content = ssePayload,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "text/event-stream"),
            )
        }
        val httpClient = HttpClientFactory.create(testConfig, engine)
        val client = SumoPodAIClient(testConfig, httpClient)

        val request = ChatCompletionRequest(
            model = "gpt-4o-mini",
            messages = listOf(ChatMessage(ChatRole.User, "Hi")),
        )
        val chunks = client.chatCompletionStream(request).toList()
        assertEquals(4, chunks.size)
    }

    @Test
    fun streamingCollectContentConcatenates() = runTest {
        val engine = MockEngine { _ ->
            respond(
                content = ssePayload,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "text/event-stream"),
            )
        }
        val httpClient = HttpClientFactory.create(testConfig, engine)
        val client = SumoPodAIClient(testConfig, httpClient)

        val request = ChatCompletionRequest(
            model = "gpt-4o-mini",
            messages = listOf(ChatMessage(ChatRole.User, "Hi")),
        )
        val content = client.chatCompletionStream(request).collectContent()
        assertEquals("Hello World", content)
    }
}
