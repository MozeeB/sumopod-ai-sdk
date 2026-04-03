package com.cikup.sumopod.ai

import com.cikup.sumopod.ai.internal.HttpClientFactory
import com.cikup.sumopod.ai.internal.InputValidator
import com.cikup.sumopod.ai.model.ChatCompletionChunk
import com.cikup.sumopod.ai.model.ChatCompletionRequest
import com.cikup.sumopod.ai.model.ChatCompletionResponse
import com.cikup.sumopod.ai.model.ChatMessage
import com.cikup.sumopod.ai.model.ChatRole
import com.cikup.sumopod.ai.model.EmbeddingRequest
import com.cikup.sumopod.ai.model.EmbeddingResponse
import com.cikup.sumopod.ai.model.ModelInfo
import com.cikup.sumopod.ai.model.ModelList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.fold
import kotlin.concurrent.Volatile

/**
 * Single entry point for the Sumopod AI SDK.
 *
 * Usage:
 * ```kotlin
 * // Initialize once
 * Sumopod.init("sk-your-api-key")
 *
 * // Or with config
 * Sumopod.init("sk-your-api-key") {
 *     baseUrl = "https://ai.sumopod.com/v1"
 *     timeout { connect = 15_000; request = 120_000 }
 * }
 *
 * // Use anywhere
 * val response = Sumopod.chatCompletion(request)
 * Sumopod.chatCompletionStream(request).collect { chunk -> ... }
 * val models = Sumopod.models()
 * ```
 */
public object Sumopod {

    @Volatile
    private var client: SumopodAIClient? = null

    @Volatile
    private var config: SumopodConfig? = null

    /**
     * Initialize the SDK with an API key.
     * Must be called before any other method. Thread-safe.
     */
    public fun init(
        apiKey: String,
        block: SumopodConfigBuilder.() -> Unit = {},
    ) {
        InputValidator.validateApiKey(apiKey)
        val newConfig = SumopodConfigBuilder(apiKey).apply(block).build()
        InputValidator.validateBaseUrl(newConfig.baseUrl)

        val oldClient = client
        val httpClient = HttpClientFactory.create(newConfig)
        val newClient = SumopodAIClient(newConfig, httpClient)

        // Atomic swap
        config = newConfig
        client = newClient

        // Close old client after swap
        oldClient?.close()
    }

    /**
     * Returns true if the SDK has been initialized with an API key.
     */
    public val isInitialized: Boolean
        get() = client != null

    /**
     * Send a chat completion request and get a full response.
     */
    public suspend fun chatCompletion(request: ChatCompletionRequest): ChatCompletionResponse =
        requireClient().chatCompletion(request)

    /**
     * Send a chat completion request and receive streaming chunks via Flow.
     */
    public fun chatCompletionStream(request: ChatCompletionRequest): Flow<ChatCompletionChunk> =
        requireClient().chatCompletionStream(request)

    /**
     * Shorthand for a simple chat completion.
     */
    public suspend fun chat(
        model: String,
        vararg messages: Pair<ChatRole, String>,
        maxTokens: Int? = null,
        temperature: Double? = null,
    ): ChatCompletionResponse = chatCompletion(
        ChatCompletionRequest(
            model = model,
            messages = messages.map { (role, content) -> ChatMessage(role, content) },
            maxTokens = maxTokens,
            temperature = temperature,
        )
    )

    /**
     * Create embeddings for the given input.
     */
    public suspend fun embeddings(request: EmbeddingRequest): EmbeddingResponse =
        requireClient().embeddings(request)

    /**
     * List all available models.
     */
    public suspend fun models(): ModelList =
        requireClient().models()

    /**
     * Get a specific model by ID.
     */
    public suspend fun model(id: String): ModelInfo =
        requireClient().model(id)

    /**
     * Release resources. Call when done using the SDK.
     */
    public fun close() {
        val oldClient = client
        client = null
        config = null
        oldClient?.close()
    }

    private fun requireClient(): SumopodAIClient =
        client ?: throw IllegalStateException(
            "Sumopod SDK not initialized. Call Sumopod.init(\"sk-your-api-key\") first."
        )
}

/**
 * Collect all streaming content from a [ChatCompletionChunk] Flow into a single string.
 */
public suspend fun Flow<ChatCompletionChunk>.collectContent(): String =
    fold("") { acc, chunk ->
        acc + (chunk.choices.firstOrNull()?.delta?.content.orEmpty())
    }
