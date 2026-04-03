package com.cikup.sumopod.ai

import com.cikup.sumopod.ai.model.ChatCompletionChunk
import com.cikup.sumopod.ai.model.ChatCompletionRequest
import com.cikup.sumopod.ai.model.ChatCompletionResponse
import com.cikup.sumopod.ai.model.ChatMessage
import com.cikup.sumopod.ai.model.ChatRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.fold

suspend fun Flow<ChatCompletionChunk>.collectContent(): String =
    fold("") { acc, chunk ->
        acc + (chunk.choices.firstOrNull()?.delta?.content.orEmpty())
    }

suspend fun SumoPodAI.chat(
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
