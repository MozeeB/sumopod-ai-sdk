package com.cikup.sumopod.ai

data class SumoPodConfig(
    val apiKey: String,
    val baseUrl: String = DEFAULT_BASE_URL,
    val connectTimeoutMs: Long = 10_000L,
    val requestTimeoutMs: Long = 60_000L,
    val socketTimeoutMs: Long = 30_000L,
    val logLevel: LogLevel = LogLevel.INFO,
    val enableCache: Boolean = false,
    val certificatePins: List<String> = emptyList(),
) {
    enum class LogLevel { NONE, INFO, HEADERS, BODY }

    override fun toString(): String =
        "SumoPodConfig(apiKey=sk-***, baseUrl=$baseUrl, logLevel=$logLevel, enableCache=$enableCache)"

    companion object {
        const val DEFAULT_BASE_URL = "https://ai.sumopod.com/v1"
    }
}

class SumoPodConfigBuilder(private val apiKey: String) {
    var baseUrl: String = SumoPodConfig.DEFAULT_BASE_URL
    var connectTimeoutMs: Long = 10_000L
    var requestTimeoutMs: Long = 60_000L
    var socketTimeoutMs: Long = 30_000L
    var logLevel: SumoPodConfig.LogLevel = SumoPodConfig.LogLevel.INFO
    var enableCache: Boolean = false
    var certificatePins: List<String> = emptyList()

    fun timeout(block: TimeoutBuilder.() -> Unit) {
        val builder = TimeoutBuilder().apply(block)
        connectTimeoutMs = builder.connect
        requestTimeoutMs = builder.request
        socketTimeoutMs = builder.socket
    }

    fun build(): SumoPodConfig = SumoPodConfig(
        apiKey = apiKey,
        baseUrl = baseUrl,
        connectTimeoutMs = connectTimeoutMs,
        requestTimeoutMs = requestTimeoutMs,
        socketTimeoutMs = socketTimeoutMs,
        logLevel = logLevel,
        enableCache = enableCache,
        certificatePins = certificatePins,
    )
}

class TimeoutBuilder {
    var connect: Long = 10_000L
    var request: Long = 60_000L
    var socket: Long = 30_000L
}
