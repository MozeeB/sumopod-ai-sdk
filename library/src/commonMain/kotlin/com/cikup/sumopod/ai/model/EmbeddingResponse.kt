package com.cikup.sumopod.ai.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class EmbeddingResponse(
    @SerialName("object") val objectType: String,
    val data: List<EmbeddingData>,
    val model: String,
    val usage: EmbeddingUsage,
) {
    @Serializable
    public data class EmbeddingData(
        @SerialName("object") val objectType: String,
        val index: Int,
        val embedding: List<Float>,
    )

    @Serializable
    public data class EmbeddingUsage(
        @SerialName("prompt_tokens") val promptTokens: Int,
        @SerialName("total_tokens") val totalTokens: Int,
    )
}
