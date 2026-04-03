package com.cikup.sumopod.ai.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ModelList(
    @SerialName("object") val objectType: String = "list",
    val data: List<ModelInfo>,
)

@Serializable
public data class ModelInfo(
    val id: String,
    @SerialName("object") val objectType: String = "model",
    val created: Long? = null,
    @SerialName("owned_by") val ownedBy: String? = null,
)
