package com.cikup.sumopod.ai

import com.cikup.sumopod.ai.internal.HttpClientFactory
import com.cikup.sumopod.ai.internal.InputValidator
import com.cikup.sumopod.ai.model.ChatCompletionChunk
import com.cikup.sumopod.ai.model.ChatCompletionRequest
import com.cikup.sumopod.ai.model.ChatCompletionResponse
import com.cikup.sumopod.ai.model.EmbeddingRequest
import com.cikup.sumopod.ai.model.EmbeddingResponse
import com.cikup.sumopod.ai.model.ModelInfo
import com.cikup.sumopod.ai.model.ModelList
import kotlinx.coroutines.flow.Flow

public interface SumoPodAI {

    public suspend fun chatCompletion(request: ChatCompletionRequest): ChatCompletionResponse

    public fun chatCompletionStream(request: ChatCompletionRequest): Flow<ChatCompletionChunk>

    public suspend fun embeddings(request: EmbeddingRequest): EmbeddingResponse

    public suspend fun models(): ModelList

    public suspend fun model(id: String): ModelInfo

    public fun close()

    public companion object {
        public operator fun invoke(
            apiKey: String,
            block: SumoPodConfigBuilder.() -> Unit = {},
        ): SumoPodAI {
            InputValidator.validateApiKey(apiKey)
            val config = SumoPodConfigBuilder(apiKey).apply(block).build()
            InputValidator.validateBaseUrl(config.baseUrl)
            val httpClient = HttpClientFactory.create(config)
            return SumoPodAIClient(config, httpClient)
        }
    }
}
