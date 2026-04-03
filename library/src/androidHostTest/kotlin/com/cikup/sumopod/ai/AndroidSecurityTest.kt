package com.cikup.sumopod.ai

import com.cikup.sumopod.ai.internal.InputValidator
import com.cikup.sumopod.ai.internal.SanitizedLogger
import com.cikup.sumopod.ai.model.ChatCompletionRequest
import com.cikup.sumopod.ai.model.ChatMessage
import com.cikup.sumopod.ai.model.ChatRole
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

class AndroidSecurityTest {

    @Test
    fun configToStringRedactsApiKeyOnAndroid() {
        val config = SumoPodConfig(apiKey = "sk-androidSecretKey1234567890")
        val str = config.toString()
        assertContains(str, "sk-***")
        assertFalse(str.contains("androidSecretKey1234567890"))
    }

    @Test
    fun sanitizedLoggerRedactsKeyOnAndroid() {
        val input = "Authorization: Bearer sk-androidTestKey12345678"
        val sanitized = SanitizedLogger.sanitize(input)
        assertContains(sanitized, "sk-***")
        assertFalse(sanitized.contains("androidTestKey12345678"))
    }

    @Test
    fun httpUrlRejectedOnAndroid() {
        assertFailsWith<IllegalArgumentException> {
            SumoPodAI("sk-validKeyForAndroidTest12345") {
                baseUrl = "http://insecure.example.com/v1"
            }
        }
    }

    @Test
    fun blankApiKeyRejectedOnAndroid() {
        assertFailsWith<IllegalArgumentException> {
            SumoPodAI("")
        }
    }

    @Test
    fun shortApiKeyRejectedOnAndroid() {
        assertFailsWith<IllegalArgumentException> {
            SumoPodAI("sk-short")
        }
    }

    @Test
    fun invalidApiKeyPrefixRejectedOnAndroid() {
        assertFailsWith<IllegalArgumentException> {
            SumoPodAI("invalid-no-sk-prefix-here-long")
        }
    }

    @Test
    fun temperatureOutOfRangeRejectedOnAndroid() {
        val request = ChatCompletionRequest(
            model = "gpt-4o-mini",
            messages = listOf(ChatMessage(ChatRole.User, "Hi")),
            temperature = 5.0,
        )
        assertFailsWith<IllegalArgumentException> {
            InputValidator.validateChatRequest(request)
        }
    }

    @Test
    fun emptyMessagesRejectedOnAndroid() {
        val request = ChatCompletionRequest(
            model = "gpt-4o-mini",
            messages = emptyList(),
        )
        assertFailsWith<IllegalArgumentException> {
            InputValidator.validateChatRequest(request)
        }
    }

    @Test
    fun blankModelRejectedOnAndroid() {
        assertFailsWith<IllegalArgumentException> {
            InputValidator.validateModelId("")
        }
    }

    @Test
    fun validConfigCreatesClientOnAndroid() {
        val client = SumoPodAI("sk-validKeyForAndroidTest12345")
        // should not throw
        client.close()
    }
}
