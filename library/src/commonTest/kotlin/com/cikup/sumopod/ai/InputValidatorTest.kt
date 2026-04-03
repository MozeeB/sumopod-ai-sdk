package com.cikup.sumopod.ai

import com.cikup.sumopod.ai.internal.InputValidator
import com.cikup.sumopod.ai.model.ChatCompletionRequest
import com.cikup.sumopod.ai.model.ChatMessage
import com.cikup.sumopod.ai.model.ChatRole
import kotlin.test.Test
import kotlin.test.assertFailsWith

class InputValidatorTest {

    @Test
    fun validApiKeyPasses() {
        InputValidator.validateApiKey("sk-validKeyForTestingPurpose123")
    }

    @Test
    fun blankApiKeyFails() {
        assertFailsWith<IllegalArgumentException> {
            InputValidator.validateApiKey("   ")
        }
    }

    @Test
    fun httpsUrlPasses() {
        InputValidator.validateBaseUrl("https://ai.sumopod.com/v1")
    }

    @Test
    fun httpUrlFails() {
        assertFailsWith<IllegalArgumentException> {
            InputValidator.validateBaseUrl("http://ai.sumopod.com/v1")
        }
    }

    @Test
    fun blankModelNameFails() {
        val request = ChatCompletionRequest(
            model = "",
            messages = listOf(ChatMessage(ChatRole.User, "Hi")),
        )
        assertFailsWith<IllegalArgumentException> {
            InputValidator.validateChatRequest(request)
        }
    }

    @Test
    fun emptyMessagesFails() {
        val request = ChatCompletionRequest(
            model = "gpt-4o-mini",
            messages = emptyList(),
        )
        assertFailsWith<IllegalArgumentException> {
            InputValidator.validateChatRequest(request)
        }
    }

    @Test
    fun temperatureOutOfRangeFails() {
        val request = ChatCompletionRequest(
            model = "gpt-4o-mini",
            messages = listOf(ChatMessage(ChatRole.User, "Hi")),
            temperature = 3.0,
        )
        assertFailsWith<IllegalArgumentException> {
            InputValidator.validateChatRequest(request)
        }
    }

    @Test
    fun topPOutOfRangeFails() {
        val request = ChatCompletionRequest(
            model = "gpt-4o-mini",
            messages = listOf(ChatMessage(ChatRole.User, "Hi")),
            topP = 1.5,
        )
        assertFailsWith<IllegalArgumentException> {
            InputValidator.validateChatRequest(request)
        }
    }

    @Test
    fun negativeMaxTokensFails() {
        val request = ChatCompletionRequest(
            model = "gpt-4o-mini",
            messages = listOf(ChatMessage(ChatRole.User, "Hi")),
            maxTokens = -1,
        )
        assertFailsWith<IllegalArgumentException> {
            InputValidator.validateChatRequest(request)
        }
    }

    @Test
    fun validRequestPasses() {
        val request = ChatCompletionRequest(
            model = "gpt-4o-mini",
            messages = listOf(ChatMessage(ChatRole.User, "Hi")),
            temperature = 0.7,
            maxTokens = 100,
        )
        InputValidator.validateChatRequest(request)
    }

    @Test
    fun blankEmbeddingInputFails() {
        assertFailsWith<IllegalArgumentException> {
            InputValidator.validateEmbeddingInput("")
        }
    }

    @Test
    fun blankModelIdFails() {
        assertFailsWith<IllegalArgumentException> {
            InputValidator.validateModelId("")
        }
    }
}
