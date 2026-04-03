package com.cikup.sumopod.ai.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatCompletionChunk(
    val id: String,
    @SerialName("object") val objectType: String,
    val created: Long,
    val model: String,
    val choices: List<ChunkChoice>,
) {
    @Serializable
    data class ChunkChoice(
        val index: Int,
        val delta: ChatDelta,
        @SerialName("finish_reason") val finishReason: String? = null,
    )

    @Serializable
    data class ChatDelta(
        val role: ChatRole? = null,
        val content: String? = null,
    )
}
