# Sumopod AI SDK

Kotlin Multiplatform SDK for the [Sumopod AI API](https://sumopod.com/dashboard/ai/quickstart). OpenAI-compatible with 40+ models across multiple providers.

## Features

- Chat completions (streaming and non-streaming)
- Embeddings
- Model listing
- Kotlin Multiplatform: Android, iOS, JVM
- SSE streaming via Kotlin Flow
- Single entry point: `Sumopod` object
- Security: API key redaction, HTTPS-only, input validation

## Quick Start

### 1. Initialize (once)

```kotlin
Sumopod.init("sk-your-api-key")

// Or with config
Sumopod.init("sk-your-api-key") {
    baseUrl = "https://ai.sumopod.com/v1"
    timeout { connect = 15_000; request = 120_000 }
    logLevel = SumopodConfig.LogLevel.HEADERS
}
```

### 2. Chat Completion

```kotlin
val response = Sumopod.chatCompletion(
    ChatCompletionRequest(
        model = "gpt-4o-mini",
        messages = listOf(
            ChatMessage(role = ChatRole.User, content = "Hello!")
        ),
        maxTokens = 150,
    )
)
println(response.choices.first().message.content)
```

### 3. Streaming

```kotlin
Sumopod.chatCompletionStream(request).collect { chunk ->
    print(chunk.choices.firstOrNull()?.delta?.content.orEmpty())
}

// Or collect all at once
val fullText = Sumopod.chatCompletionStream(request).collectContent()
```

### 4. Shorthand

```kotlin
val response = Sumopod.chat(
    "gpt-4o-mini",
    ChatRole.User to "What is Kotlin Multiplatform?",
    maxTokens = 200,
)
```

### 5. Embeddings

```kotlin
val response = Sumopod.embeddings(
    EmbeddingRequest(model = "text-embedding-3-small", input = "Hello world")
)
```

### 6. List Models

```kotlin
val models = Sumopod.models()
models.data.forEach { println(it.id) }
```

### 7. Cleanup

```kotlin
Sumopod.close()
```

## Available Models

40+ models across providers:

| Provider | Models |
|---|---|
| OpenAI | gpt-4o, gpt-4o-mini, gpt-4.1, gpt-5, gpt-5.1, gpt-5.2 |
| Anthropic | claude-haiku-4-5 |
| Google | gemini-2.0-flash, gemini-2.5-flash, gemini-2.5-pro, gemini-3-flash |
| BytePlus | deepseek-r1, deepseek-v3-2, seed-2-0-pro, kimi-k2 |
| Z.AI | glm-5, glm-5-code, glm-5-turbo, glm-5.1 |

See full list at [Sumopod Models](https://sumopod.com/dashboard/ai/models).

## Sample App

Compose Multiplatform sample with Chat, Models, and Settings screens.

```bash
# Desktop
./gradlew :sample:run

# Android
./gradlew :sample:assembleDebug
```

## Platform Support

| Platform | Engine | Min Version |
|---|---|---|
| Android | OkHttp | API 24 |
| iOS | Darwin (NSURLSession) | iOS 13 |
| JVM | CIO | Java 11 |

## Security

- API keys never logged or serialized
- HTTPS-only (http:// URLs rejected)
- All inputs validated before API calls
- Error messages never contain sensitive data
- `SumopodConfig.toString()` redacts API key

## Dependencies

| Library | Version |
|---|---|
| Kotlin | 2.2.20 |
| Ktor | 3.3.1 |
| kotlinx.serialization | 1.9.0 |
| kotlinx.coroutines | 1.10.2 |
| Compose Multiplatform | 1.10.3 |

## License

Apache License 2.0
