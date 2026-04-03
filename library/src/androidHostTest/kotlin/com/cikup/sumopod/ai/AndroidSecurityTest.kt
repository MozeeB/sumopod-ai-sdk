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
import kotlin.test.assertTrue

class AndroidSecurityTest {

    @Test
    fun configToStringRedactsApiKeyOnAndroid() {
        val config = SumopodConfig(apiKey = "sk-androidSecretKey1234567890")
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
            Sumopod.init("sk-validKeyForAndroidTest12345") {
                baseUrl = "http://insecure.example.com/v1"
            }
        }
    }

    @Test
    fun blankApiKeyRejectedOnAndroid() {
        assertFailsWith<IllegalArgumentException> {
            Sumopod.init("")
        }
    }

    @Test
    fun shortApiKeyRejectedOnAndroid() {
        assertFailsWith<IllegalArgumentException> {
            Sumopod.init("sk-short")
        }
    }

    @Test
    fun invalidApiKeyPrefixRejectedOnAndroid() {
        assertFailsWith<IllegalArgumentException> {
            Sumopod.init("invalid-no-sk-prefix-here-long")
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
    fun validConfigInitializesOnAndroid() {
        Sumopod.init("sk-validKeyForAndroidTest12345")
        assertTrue(Sumopod.isInitialized)
        Sumopod.close()
    }
}
