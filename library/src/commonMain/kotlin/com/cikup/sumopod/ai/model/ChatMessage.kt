package com.cikup.sumopod.ai.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class ChatRole {
    @SerialName("system") System,
    @SerialName("user") User,
    @SerialName("assistant") Assistant,
    @SerialName("tool") Tool,
}

@Serializable
public data class ChatMessage(
    val role: ChatRole,
    val content: String,
    val name: String? = null,
)
