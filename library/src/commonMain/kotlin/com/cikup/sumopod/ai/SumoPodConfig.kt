package com.cikup.sumopod.ai

public data class SumopodConfig(
    val apiKey: String,
    val baseUrl: String = DEFAULT_BASE_URL,
    val connectTimeoutMs: Long = 10_000L,
    val requestTimeoutMs: Long = 60_000L,
    val socketTimeoutMs: Long = 30_000L,
    val logLevel: LogLevel = LogLevel.INFO,
    val certificatePins: List<String> = emptyList(),
) {
    public enum class LogLevel { NONE, INFO, HEADERS, BODY }

    override fun toString(): String =
        "SumopodConfig(apiKey=sk-***, baseUrl=$baseUrl, logLevel=$logLevel)"

    public companion object {
        public const val DEFAULT_BASE_URL: String = "https://ai.sumopod.com/v1"
    }
}

public class SumopodConfigBuilder(private val apiKey: String) {
    public var baseUrl: String = SumopodConfig.DEFAULT_BASE_URL
    public var connectTimeoutMs: Long = 10_000L
    public var requestTimeoutMs: Long = 60_000L
    public var socketTimeoutMs: Long = 30_000L
    public var logLevel: SumopodConfig.LogLevel = SumopodConfig.LogLevel.INFO
    public var certificatePins: List<String> = emptyList()

    public fun timeout(block: TimeoutBuilder.() -> Unit) {
        val builder = TimeoutBuilder().apply(block)
        connectTimeoutMs = builder.connect
        requestTimeoutMs = builder.request
        socketTimeoutMs = builder.socket
    }

    public fun build(): SumopodConfig = SumopodConfig(
        apiKey = apiKey,
        baseUrl = baseUrl,
        connectTimeoutMs = connectTimeoutMs,
        requestTimeoutMs = requestTimeoutMs,
        socketTimeoutMs = socketTimeoutMs,
        logLevel = logLevel,
        certificatePins = certificatePins,
    )
}

public class TimeoutBuilder {
    public var connect: Long = 10_000L
    public var request: Long = 60_000L
    public var socket: Long = 30_000L
}
