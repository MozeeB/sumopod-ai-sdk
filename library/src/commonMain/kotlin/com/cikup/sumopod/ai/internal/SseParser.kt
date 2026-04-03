package com.cikup.sumopod.ai.internal

import com.cikup.sumopod.ai.model.ChatCompletionChunk
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

internal object SseParser {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Suppress("DEPRECATION")
    fun parse(channel: ByteReadChannel): Flow<ChatCompletionChunk> = flow {
        while (!channel.isClosedForRead) {
            val line = channel.readUTF8Line() ?: break

            if (line.isBlank() || line.startsWith(":")) continue

            if (!line.startsWith("data: ")) continue

            val data = line.removePrefix("data: ").trim()

            if (data == "[DONE]") break

            val chunk = json.decodeFromString<ChatCompletionChunk>(data)
            emit(chunk)
        }
    }
}
