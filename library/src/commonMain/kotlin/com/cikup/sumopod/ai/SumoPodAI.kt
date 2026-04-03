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

interface SumoPodAI {

    suspend fun chatCompletion(request: ChatCompletionRequest): ChatCompletionResponse

    fun chatCompletionStream(request: ChatCompletionRequest): Flow<ChatCompletionChunk>

    suspend fun embeddings(request: EmbeddingRequest): EmbeddingResponse

    suspend fun models(): ModelList

    suspend fun model(id: String): ModelInfo

    fun close()

    companion object {
        operator fun invoke(
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
