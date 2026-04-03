package com.cikup.sumopod.ai

import com.cikup.sumopod.ai.error.ErrorResponse
import com.cikup.sumopod.ai.error.SumoPodException
import com.cikup.sumopod.ai.internal.ApiRoutes
import com.cikup.sumopod.ai.internal.InputValidator
import com.cikup.sumopod.ai.internal.SseParser
import com.cikup.sumopod.ai.model.ChatCompletionChunk
import com.cikup.sumopod.ai.model.ChatCompletionRequest
import com.cikup.sumopod.ai.model.ChatCompletionResponse
import com.cikup.sumopod.ai.model.EmbeddingRequest
import com.cikup.sumopod.ai.model.EmbeddingResponse
import com.cikup.sumopod.ai.model.ModelInfo
import com.cikup.sumopod.ai.model.ModelList
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

internal class SumoPodAIClient(
    private val config: SumoPodConfig,
    private val httpClient: HttpClient,
) : SumoPodAI {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun chatCompletion(request: ChatCompletionRequest): ChatCompletionResponse {
        InputValidator.validateChatRequest(request)
        val nonStreamRequest = request.copy(stream = false)
        val response = httpClient.post(ApiRoutes.CHAT_COMPLETIONS) {
            setBody(nonStreamRequest)
        }
        handleErrors(response)
        return response.body()
    }

    override fun chatCompletionStream(request: ChatCompletionRequest): Flow<ChatCompletionChunk> = flow {
        InputValidator.validateChatRequest(request)
        val streamRequest = request.copy(stream = true)
        httpClient.preparePost(ApiRoutes.CHAT_COMPLETIONS) {
            setBody(streamRequest)
        }.execute { response ->
            handleErrors(response)
            val channel = response.body<io.ktor.utils.io.ByteReadChannel>()
            SseParser.parse(channel).collect { chunk ->
                emit(chunk)
            }
        }
    }

    override suspend fun embeddings(request: EmbeddingRequest): EmbeddingResponse {
        InputValidator.validateEmbeddingInput(request.input)
        val response = httpClient.post(ApiRoutes.EMBEDDINGS) {
            setBody(request)
        }
        handleErrors(response)
        return response.body()
    }

    override suspend fun models(): ModelList {
        val response = httpClient.get(ApiRoutes.MODELS)
        handleErrors(response)
        return response.body()
    }

    override suspend fun model(id: String): ModelInfo {
        InputValidator.validateModelId(id)
        val response = httpClient.get("${ApiRoutes.MODELS}/$id")
        handleErrors(response)
        return response.body()
    }

    override fun close() {
        httpClient.close()
    }

    private suspend fun handleErrors(response: HttpResponse) {
        if (response.status.isSuccess()) return

        val errorParam = try {
            val body = response.bodyAsText()
            val errorResponse = json.decodeFromString<ErrorResponse>(body)
            errorResponse.error?.param
        } catch (_: Exception) {
            null
        }

        throw when (response.status) {
            HttpStatusCode.Unauthorized -> SumoPodException.AuthenticationException()
            HttpStatusCode.TooManyRequests -> {
                val retryAfter = response.headers["Retry-After"]?.toLongOrNull()?.let { it * 1000 }
                SumoPodException.RateLimitException(retryAfter)
            }
            HttpStatusCode.BadRequest,
            HttpStatusCode(422, "Unprocessable Entity") ->
                SumoPodException.InvalidRequestException(errorParam)
            else -> SumoPodException.ApiException(response.status.value)
        }
    }
}
