# SumoPod AI SDK

Kotlin Multiplatform SDK for the [SumoPod AI API](https://sumopod.com/dashboard/ai/quickstart). OpenAI-compatible with 40+ models across multiple providers.

## Features

- Chat completions (streaming and non-streaming)
- Embeddings
- Model listing
- Kotlin Multiplatform: Android, iOS, JVM
- SSE streaming via Kotlin Flow
- Koin dependency injection
- Room KMP caching (opt-in)
- Security: API key redaction, HTTPS-only, input validation

## Installation

Add the dependency to your `build.gradle.kts`:

```kotlin
// In your version catalog (libs.versions.toml)
[libraries]
sumopod-ai-sdk = { module = "com.cikup.sumopod.ai:sumopod-ai-sdk", version = "0.1.0" }

// In your module's build.gradle.kts
dependencies {
    implementation(libs.sumopod.ai.sdk)
}
```

## Quick Start

### Setup

1. Get an API key from [SumoPod Dashboard](https://sumopod.com/dashboard/ai/quickstart)
2. Create a client:

```kotlin
val client = SumoPodAI("sk-your-api-key")
```

### Chat Completion

```kotlin
val response = client.chatCompletion(
    ChatCompletionRequest(
        model = "gpt-4o-mini",
        messages = listOf(
            ChatMessage(role = ChatRole.User, content = "Hello!")
        ),
        maxTokens = 150,
        temperature = 0.7,
    )
)
println(response.choices.first().message.content)
```

### Streaming

```kotlin
client.chatCompletionStream(request).collect { chunk ->
    print(chunk.choices.firstOrNull()?.delta?.content.orEmpty())
}

// Or collect all content at once
val fullText = client.chatCompletionStream(request).collectContent()
```

### Shorthand

```kotlin
val response = client.chat(
    "gpt-4o-mini",
    ChatRole.User to "What is Kotlin Multiplatform?",
    maxTokens = 200,
)
```

### Embeddings

```kotlin
val response = client.embeddings(
    EmbeddingRequest(model = "text-embedding-3-small", input = "Hello world")
)
```

### List Models

```kotlin
val models = client.models()
models.data.forEach { println(it.id) }
```

## Configuration

```kotlin
val client = SumoPodAI("sk-your-api-key") {
    baseUrl = "https://ai.sumopod.com/v1"  // default
    enableCache = true                      // opt-in Room caching

    timeout {
        connect = 10_000   // 10s
        request = 60_000   // 60s
        socket = 30_000    // 30s
    }

    logLevel = SumoPodConfig.LogLevel.HEADERS  // NONE, INFO, HEADERS, BODY
}
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

See full list at [SumoPod Models](https://sumopod.com/dashboard/ai/models).

## Sample App

The project includes a Compose Multiplatform sample app with:

- **Chat screen** -- real-time streaming chat with model selector
- **Models screen** -- browse all available models
- **Settings screen** -- API key configuration

### Run the sample

```bash
# Desktop
./gradlew :sample:run

# Android
./gradlew :sample:assembleDebug

# iOS -- open in Xcode via iosApp/
```

## Platform Support

| Platform | Engine | Min Version |
|---|---|---|
| Android | OkHttp | API 24 |
| iOS | Darwin (NSURLSession) | iOS 13 |
| JVM | CIO | Java 11 |

## Security

- API keys are never logged or serialized
- HTTPS-only (http:// URLs rejected)
- All inputs validated before API calls
- Error messages never contain sensitive data
- `SumoPodConfig.toString()` redacts API key

## Dependencies

| Library | Version |
|---|---|
| Kotlin | 2.2.20 |
| Ktor | 3.3.1 |
| kotlinx.serialization | 1.9.0 |
| kotlinx.coroutines | 1.10.2 |
| Koin | 4.0.0 |
| Room KMP | 2.7.1 |
| Compose Multiplatform | 1.10.3 |

## Publishing

Configured for Maven Central via [vanniktech-maven-publish](https://github.com/vanniktech/gradle-maven-publish-plugin).

```bash
./gradlew publishToMavenCentral
```

## License

Apache License 2.0 -- see [LICENSE](LICENSE) for details.
