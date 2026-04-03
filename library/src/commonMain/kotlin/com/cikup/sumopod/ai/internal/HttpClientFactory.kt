package com.cikup.sumopod.ai.internal

import com.cikup.sumopod.ai.SumopodConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal expect fun createPlatformEngine(): HttpClientEngine

internal object HttpClientFactory {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = false
        isLenient = true
    }

    fun create(config: SumopodConfig): HttpClient {
        val engine = createPlatformEngine()
        return create(config, engine)
    }

    fun create(config: SumopodConfig, engine: HttpClientEngine): HttpClient =
        HttpClient(engine) {
            install(ContentNegotiation) {
                json(json)
            }

            install(HttpTimeout) {
                connectTimeoutMillis = config.connectTimeoutMs
                requestTimeoutMillis = config.requestTimeoutMs
                socketTimeoutMillis = config.socketTimeoutMs
            }

            install(Logging) {
                logger = SanitizedLogger()
                level = when (config.logLevel) {
                    SumopodConfig.LogLevel.NONE -> LogLevel.NONE
                    SumopodConfig.LogLevel.INFO -> LogLevel.INFO
                    SumopodConfig.LogLevel.HEADERS -> LogLevel.HEADERS
                    SumopodConfig.LogLevel.BODY -> LogLevel.BODY
                }
                sanitizeHeader { header ->
                    header == HttpHeaders.Authorization
                }
            }

            defaultRequest {
                url(config.baseUrl.trimEnd('/') + "/")
                contentType(ContentType.Application.Json)
                headers.append(HttpHeaders.Authorization, "Bearer ${config.apiKey}")
                headers.append("User-Agent", "Sumopod-AI-SDK/0.1.0 (KMP)")
            }
        }
}
