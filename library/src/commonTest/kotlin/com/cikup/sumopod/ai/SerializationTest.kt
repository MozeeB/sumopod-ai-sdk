package com.cikup.sumopod.ai

import com.cikup.sumopod.ai.model.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class SerializationTest {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = false
    }

    @Test
    fun chatMessageRoundTrip() {
        val message = ChatMessage(role = ChatRole.User, content = "Hello")
        val encoded = json.encodeToString(message)
        val decoded = json.decodeFromString<ChatMessage>(encoded)
        assertEquals(message, decoded)
    }

    @Test
    fun chatRoleSerializesToSnakeCase() {
        val encoded = json.encodeToString(ChatRole.User)
        assertEquals("\"user\"", encoded)
    }

    @Test
    fun chatCompletionRequestRoundTrip() {
        val request = ChatCompletionRequest(
            model = "gpt-4o-mini",
            messages = listOf(ChatMessage(ChatRole.User, "Hi")),
            maxTokens = 100,
            temperature = 0.7,
        )
        val encoded = json.encodeToString(request)
        val decoded = json.decodeFromString<ChatCompletionRequest>(encoded)
        assertEquals(request, decoded)
    }

    @Test
    fun chatCompletionResponseDeserialization() {
        val responseJson = """
        {
            "id": "chatcmpl-123",
            "object": "chat.completion",
            "created": 1700000000,
            "model": "gpt-4o-mini",
            "choices": [{
                "index": 0,
                "message": {"role": "assistant", "content": "Hello!"},
                "finish_reason": "stop"
            }],
            "usage": {"prompt_tokens": 10, "completion_tokens": 5, "total_tokens": 15}
        }
        """.trimIndent()
        val response = json.decodeFromString<ChatCompletionResponse>(responseJson)
        assertEquals("chatcmpl-123", response.id)
        assertEquals("Hello!", response.choices.first().message.content)
        assertEquals(15, response.usage?.totalTokens)
    }

    @Test
    fun chatCompletionChunkDeserialization() {
        val chunkJson = """
        {
            "id": "chatcmpl-123",
            "object": "chat.completion.chunk",
            "created": 1700000000,
            "model": "gpt-4o-mini",
            "choices": [{
                "index": 0,
                "delta": {"content": "Hello"},
                "finish_reason": null
            }]
        }
        """.trimIndent()
        val chunk = json.decodeFromString<ChatCompletionChunk>(chunkJson)
        assertEquals("Hello", chunk.choices.first().delta.content)
    }

    @Test
    fun embeddingRequestRoundTrip() {
        val request = EmbeddingRequest(model = "text-embedding-3-small", input = "test")
        val encoded = json.encodeToString(request)
        val decoded = json.decodeFromString<EmbeddingRequest>(encoded)
        assertEquals(request, decoded)
    }

    @Test
    fun modelListDeserialization() {
        val modelsJson = """
        {
            "object": "list",
            "data": [
                {"id": "gpt-4o-mini", "object": "model", "created": 1700000000, "owned_by": "openai"},
                {"id": "claude-haiku-4-5", "object": "model", "owned_by": "anthropic"}
            ]
        }
        """.trimIndent()
        val models = json.decodeFromString<ModelList>(modelsJson)
        assertEquals(2, models.data.size)
        assertEquals("gpt-4o-mini", models.data[0].id)
    }

    @Test
    fun unknownFieldsIgnored() {
        val responseJson = """
        {
            "id": "chatcmpl-123",
            "object": "chat.completion",
            "created": 1700000000,
            "model": "gpt-4o-mini",
            "choices": [{
                "index": 0,
                "message": {"role": "assistant", "content": "Hi"},
                "finish_reason": "stop",
                "unknown_field": true
            }],
            "system_fingerprint": "fp_abc123"
        }
        """.trimIndent()
        val response = json.decodeFromString<ChatCompletionResponse>(responseJson)
        assertEquals("Hi", response.choices.first().message.content)
    }
}
