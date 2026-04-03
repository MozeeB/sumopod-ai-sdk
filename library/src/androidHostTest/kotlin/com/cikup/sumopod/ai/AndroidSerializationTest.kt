package com.cikup.sumopod.ai

import com.cikup.sumopod.ai.model.ChatCompletionChunk
import com.cikup.sumopod.ai.model.ChatCompletionRequest
import com.cikup.sumopod.ai.model.ChatCompletionResponse
import com.cikup.sumopod.ai.model.ChatMessage
import com.cikup.sumopod.ai.model.ChatRole
import com.cikup.sumopod.ai.model.EmbeddingRequest
import com.cikup.sumopod.ai.model.ModelList
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AndroidSerializationTest {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = false
    }

    @Test
    fun chatMessageRoundTripOnAndroid() {
        val message = ChatMessage(role = ChatRole.User, content = "Hello from Android")
        val encoded = json.encodeToString(message)
        val decoded = json.decodeFromString<ChatMessage>(encoded)
        assertEquals(message, decoded)
    }

    @Test
    fun chatCompletionRequestSerializesCorrectly() {
        val request = ChatCompletionRequest(
            model = "gpt-4o-mini",
            messages = listOf(
                ChatMessage(ChatRole.System, "You are a helpful assistant"),
                ChatMessage(ChatRole.User, "Hello"),
            ),
            maxTokens = 150,
            temperature = 0.7,
            stream = true,
        )
        val encoded = json.encodeToString(request)
        assert(encoded.contains("\"max_tokens\":150"))
        assert(encoded.contains("\"stream\":true"))
        assert(encoded.contains("\"system\""))
    }

    @Test
    fun chatCompletionResponseParsesOnAndroid() {
        val responseJson = """
        {
            "id": "chatcmpl-android-test",
            "object": "chat.completion",
            "created": 1700000000,
            "model": "gpt-4o-mini",
            "choices": [{
                "index": 0,
                "message": {"role": "assistant", "content": "Hello from Sumopod!"},
                "finish_reason": "stop"
            }],
            "usage": {"prompt_tokens": 10, "completion_tokens": 5, "total_tokens": 15}
        }
        """.trimIndent()
        val response = json.decodeFromString<ChatCompletionResponse>(responseJson)
        assertEquals("chatcmpl-android-test", response.id)
        assertEquals(ChatRole.Assistant, response.choices.first().message.role)
        assertEquals(15, response.usage?.totalTokens)
    }

    @Test
    fun streamingChunkParsesOnAndroid() {
        val chunkJson = """
        {
            "id": "chatcmpl-stream",
            "object": "chat.completion.chunk",
            "created": 1700000000,
            "model": "gpt-4o-mini",
            "choices": [{
                "index": 0,
                "delta": {"role": "assistant", "content": "Hi"},
                "finish_reason": null
            }]
        }
        """.trimIndent()
        val chunk = json.decodeFromString<ChatCompletionChunk>(chunkJson)
        assertEquals("Hi", chunk.choices.first().delta.content)
        assertEquals(ChatRole.Assistant, chunk.choices.first().delta.role)
    }

    @Test
    fun modelListParsesOnAndroid() {
        val modelsJson = """
        {
            "object": "list",
            "data": [
                {"id": "gpt-4o-mini", "object": "model", "owned_by": "openai"},
                {"id": "claude-haiku-4-5", "object": "model", "owned_by": "anthropic"},
                {"id": "gemini/gemini-2.5-flash", "object": "model", "owned_by": "google"}
            ]
        }
        """.trimIndent()
        val models = json.decodeFromString<ModelList>(modelsJson)
        assertEquals(3, models.data.size)
        assertEquals("anthropic", models.data[1].ownedBy)
    }

    @Test
    fun embeddingRequestRoundTripOnAndroid() {
        val request = EmbeddingRequest(
            model = "text-embedding-3-small",
            input = "Test embedding on Android",
            dimensions = 512,
        )
        val encoded = json.encodeToString(request)
        val decoded = json.decodeFromString<EmbeddingRequest>(encoded)
        assertEquals(request, decoded)
    }
}
