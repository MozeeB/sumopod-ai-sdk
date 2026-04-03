package com.cikup.sumopod.ai.internal

import com.cikup.sumopod.ai.model.ChatCompletionRequest

internal object InputValidator {

    fun validateApiKey(apiKey: String) {
        require(apiKey.isNotBlank()) { "API key must not be blank" }
        require(apiKey.startsWith("sk-")) { "API key must start with 'sk-'" }
        require(apiKey.length >= 20) { "API key appears to be too short" }
    }

    fun validateBaseUrl(url: String) {
        require(url.isNotBlank()) { "Base URL must not be blank" }
        require(url.startsWith("https://")) {
            "Base URL must use HTTPS. Got: ${url.take(10)}..."
        }
        require(!url.contains(" ")) { "Base URL must not contain spaces" }
    }

    fun validateChatRequest(request: ChatCompletionRequest) {
        require(request.model.isNotBlank()) { "Model name must not be blank" }
        require(request.messages.isNotEmpty()) { "Messages list must not be empty" }

        request.maxTokens?.let { tokens ->
            require(tokens > 0) { "maxTokens must be positive, got: $tokens" }
        }
        request.temperature?.let { temp ->
            require(temp in 0.0..2.0) { "temperature must be in 0.0..2.0, got: $temp" }
        }
        request.topP?.let { p ->
            require(p in 0.0..1.0) { "topP must be in 0.0..1.0, got: $p" }
        }
        request.n?.let { n ->
            require(n > 0) { "n must be positive, got: $n" }
        }
        request.presencePenalty?.let { penalty ->
            require(penalty in -2.0..2.0) { "presencePenalty must be in -2.0..2.0, got: $penalty" }
        }
        request.frequencyPenalty?.let { penalty ->
            require(penalty in -2.0..2.0) { "frequencyPenalty must be in -2.0..2.0, got: $penalty" }
        }
    }

    fun validateEmbeddingInput(input: String) {
        require(input.isNotBlank()) { "Embedding input must not be blank" }
    }

    fun validateModelId(id: String) {
        require(id.isNotBlank()) { "Model ID must not be blank" }
    }
}
