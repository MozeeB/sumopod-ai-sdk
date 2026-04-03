package com.cikup.sumopod.ai.internal

import io.ktor.client.plugins.logging.Logger

internal class SanitizedLogger : Logger {

    override fun log(message: String) {
        println(sanitize(message))
    }

    companion object {
        private val API_KEY_PATTERN = Regex("""sk-[a-zA-Z0-9_-]{10,}""")

        fun sanitize(message: String): String =
            API_KEY_PATTERN.replace(message, "sk-***")
    }
}
