package com.cikup.sumopod.ai.model

import kotlinx.serialization.Serializable

@Serializable
public data class EmbeddingRequest(
    val model: String,
    val input: String,
    val dimensions: Int? = null,
)
