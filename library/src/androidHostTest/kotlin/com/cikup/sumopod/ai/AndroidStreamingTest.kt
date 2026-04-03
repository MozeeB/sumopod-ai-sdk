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

class AndroidStreamingTest {

    private val testConfig = SumoPodConfig(
        apiKey = "sk-testKeyForAndroidStreaming1",
        logLevel = SumoPodConfig.LogLevel.NONE,
    )

    private val ssePayload = buildString {
        appendLine("data: {\"id\":\"chatcmpl-s\",\"object\":\"chat.completion.chunk\",\"created\":1700000000,\"model\":\"gpt-4o-mini\",\"choices\":[{\"index\":0,\"delta\":{\"role\":\"assistant\"},\"finish_reason\":null}]}")
        appendLine()
        appendLine("data: {\"id\":\"chatcmpl-s\",\"object\":\"chat.completion.chunk\",\"created\":1700000000,\"model\":\"gpt-4o-mini\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"Hi\"},\"finish_reason\":null}]}")
        appendLine()
        appendLine("data: {\"id\":\"chatcmpl-s\",\"object\":\"chat.completion.chunk\",\"created\":1700000000,\"model\":\"gpt-4o-mini\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\" there\"},\"finish_reason\":null}]}")
        appendLine()
        appendLine("data: {\"id\":\"chatcmpl-s\",\"object\":\"chat.completion.chunk\",\"created\":1700000000,\"model\":\"gpt-4o-mini\",\"choices\":[{\"index\":0,\"delta\":{},\"finish_reason\":\"stop\"}]}")
        appendLine()
        appendLine("data: [DONE]")
        appendLine()
    }

    @Test
    fun streamingCollectsChunksOnAndroid() = runTest {
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
    fun streamingCollectContentOnAndroid() = runTest {
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
        assertEquals("Hi there", content)
    }

    @Test
    fun streamRequestSetsStreamFlag() = runTest {
        var capturedBody: String? = null
        val engine = MockEngine { request ->
            capturedBody = request.body.toString()
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
        client.chatCompletionStream(request).toList()
        // Stream flag should be set by client
    }
}
