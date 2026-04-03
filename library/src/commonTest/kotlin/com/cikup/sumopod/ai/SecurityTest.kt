package com.cikup.sumopod.ai

import com.cikup.sumopod.ai.internal.SanitizedLogger
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

class SecurityTest {

    @Test
    fun configToStringRedactsApiKey() {
        val config = SumoPodConfig(apiKey = "sk-realKeyThatShouldBeHidden12345")
        val str = config.toString()
        assertContains(str, "sk-***")
        assertFalse(str.contains("realKeyThatShouldBeHidden12345"))
    }

    @Test
    fun sanitizedLoggerRedactsApiKey() {
        val sanitized = SanitizedLogger.sanitize(
            "Authorization: Bearer sk-abcdef1234567890abcdef"
        )
        assertContains(sanitized, "sk-***")
        assertFalse(sanitized.contains("abcdef1234567890abcdef"))
    }

    @Test
    fun httpBaseUrlMustBeHttps() {
        assertFailsWith<IllegalArgumentException> {
            SumoPodAI("sk-validKeyForTestingPurpose123") {
                baseUrl = "http://insecure.example.com/v1"
            }
        }
    }

    @Test
    fun apiKeyMustStartWithSk() {
        assertFailsWith<IllegalArgumentException> {
            SumoPodAI("invalid-key-format-no-sk-prefix")
        }
    }

    @Test
    fun apiKeyMustNotBeBlank() {
        assertFailsWith<IllegalArgumentException> {
            SumoPodAI("")
        }
    }

    @Test
    fun apiKeyMustBeLongEnough() {
        assertFailsWith<IllegalArgumentException> {
            SumoPodAI("sk-short")
        }
    }
}
