<p align="center">
  <h1 align="center">Sumopod AI SDK</h1>
  <p align="center">
    Kotlin Multiplatform SDK for the <a href="https://sumopod.com/dashboard/ai/quickstart">Sumopod AI API</a><br/>
    OpenAI-compatible with 40+ models across multiple providers
  </p>
  <p align="center">
    <a href="https://central.sonatype.com/artifact/com.cikup.sumopod.ai/sumopod-ai-sdk"><img src="https://img.shields.io/maven-central/v/com.cikup.sumopod.ai/sumopod-ai-sdk?style=flat&color=blue&label=Maven%20Central" alt="Maven Central"/></a>
    <a href="https://github.com/MozeeB/sumopod-ai-sdk/actions/workflows/ci.yml"><img src="https://github.com/MozeeB/sumopod-ai-sdk/actions/workflows/ci.yml/badge.svg" alt="CI"/></a>
    <a href="https://www.apache.org/licenses/LICENSE-2.0"><img src="https://img.shields.io/badge/License-Apache%202.0-green.svg" alt="License"/></a>
    <img src="https://img.shields.io/badge/Kotlin-2.2.20-7F52FF?logo=kotlin&logoColor=white" alt="Kotlin"/>
    <img src="https://img.shields.io/badge/Platform-Android%20%7C%20iOS%20%7C%20JVM-orange" alt="Platform"/>
  </p>
</p>

---

## Installation

<table>
<tr><td><b>Kotlin DSL</b></td><td><b>Groovy</b></td></tr>
<tr>
<td>

```kotlin
implementation("com.cikup.sumopod.ai:sumopod-ai-sdk:0.1.1")
```

</td>
<td>

```groovy
implementation 'com.cikup.sumopod.ai:sumopod-ai-sdk:0.1.1'
```

</td>
</tr>
</table>

**Kotlin Multiplatform:**

```kotlin
commonMain.dependencies {
    implementation("com.cikup.sumopod.ai:sumopod-ai-sdk:0.1.1")
}
```

---

## Features

| Feature | Description |
|:---|:---|
| **Chat Completions** | Streaming and non-streaming responses |
| **Embeddings** | Text embedding generation |
| **Model Listing** | Browse 40+ available models |
| **Multiplatform** | Android, iOS, and JVM from a single codebase |
| **Streaming** | SSE streaming via Kotlin `Flow` with `collectContent()` |
| **Thread-Safe** | `Sumopod` singleton with `@Volatile` fields |
| **Secure** | API key redaction, HTTPS-only, input validation |
| **Tested** | 173 automated tests across all platforms |

---

## Quick Start

### 1. Initialize

```kotlin
Sumopod.init("sk-your-api-key")

// Or with custom config
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

---

## Available Models

> 40+ models across providers. See the full list at [Sumopod Models](https://sumopod.com/dashboard/ai/models).

| Provider | Models |
|:---|:---|
| **OpenAI** | gpt-4o, gpt-4o-mini, gpt-4.1, gpt-5, gpt-5.1, gpt-5.2 |
| **Anthropic** | claude-haiku-4-5 |
| **Google** | gemini-2.0-flash, gemini-2.5-flash, gemini-2.5-pro, gemini-3-flash |
| **BytePlus** | deepseek-r1, deepseek-v3-2, seed-2-0-pro, kimi-k2 |
| **Z.AI** | glm-5, glm-5-code, glm-5-turbo, glm-5.1 |

---

## Platform Support

| Platform | Engine | Min Version |
|:---|:---|:---|
| **Android** | OkHttp | API 24 |
| **iOS** | Darwin (NSURLSession) | iOS 13 |
| **JVM** | CIO | Java 11 |

---

## Sample App

Compose Multiplatform demo with Chat, Models, and Settings screens.

```bash
./gradlew :sample:run              # Desktop
./gradlew :sample:assembleDebug    # Android
open sample/iosApp/iosApp.xcodeproj  # iOS (Xcode)
```

---

## Security

- API keys never logged or serialized
- HTTPS-only (`http://` URLs rejected)
- All inputs validated before API calls
- Error messages never contain sensitive data
- `SumopodConfig.toString()` redacts API key
- Thread-safe singleton with `@Volatile` fields

---

## Testing

173 automated tests across 5 test suites:

```bash
./gradlew :library:jvmTest               # 37 tests
./gradlew :library:testAndroidHostTest    # 62 tests
./gradlew :library:iosSimulatorArm64Test  # 37 tests
./gradlew :sample:testDebugUnitTest       # 22 tests
./gradlew :sample:desktopTest             # 15 Compose UI tests
```

---

## Dependencies

| Library | Version |
|:---|:---|
| Kotlin | 2.2.20 |
| Ktor | 3.3.1 |
| kotlinx.serialization | 1.9.0 |
| kotlinx.coroutines | 1.10.2 |
| Compose Multiplatform | 1.10.3 |

---

## License

```
Copyright 2026 Cikup

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0
```
