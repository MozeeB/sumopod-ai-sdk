package com.cikup.sumopod.ai.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ChatCompletionResponse(
    val id: String,
    @SerialName("object") val objectType: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>,
    val usage: Usage? = null,
) {
    @Serializable
    public data class Choice(
        val index: Int,
        val message: ChatMessage,
        @SerialName("finish_reason") val finishReason: String? = null,
    )
}
