# CLAUDE.md — SumoPod AI SDK

## Project Overview

Kotlin Multiplatform SDK for the SumoPod AI API (OpenAI-compatible). Provides chat completions with streaming, embeddings, and model listing across Android, iOS, and JVM.

## Build Commands

```bash
# Set Java (required on macOS without JAVA_HOME)
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"

# Build library (all targets)
./gradlew :library:build

# Run JVM tests
./gradlew :library:jvmTest

# Build Android sample APK
./gradlew :sample:assembleDebug

# Run desktop sample
./gradlew :sample:run

# Compile iOS targets
./gradlew :library:compileKotlinIosArm64
./gradlew :library:compileKotlinIosSimulatorArm64

# Full build (all platforms)
./gradlew build
```

## Architecture

- **`:library`** — SDK module (commonMain + platform engines)
- **`:sample`** — Compose Multiplatform demo app (Android + iOS + Desktop)

## Key Patterns

- **Package**: `com.cikup.sumopod.ai`
- **HTTP**: Ktor 3.3.1 with platform engines (CIO/OkHttp/Darwin)
- **Serialization**: kotlinx.serialization 1.9.0 (must stay compatible with Kotlin 2.2.20)
- **DI**: Koin 4.0.0
- **Database**: Room KMP 2.7.1 with `@ConstructedBy` for iOS
- **Streaming**: SSE parser reads `ByteReadChannel` → `Flow<ChatCompletionChunk>`
- **Security**: API keys redacted in logs/toString, HTTPS-only, input validation

## Version Constraints

Kotlin 2.2.20 requires:
- Ktor ≤ 3.3.x (3.4+ needs Kotlin 2.3)
- kotlinx-serialization ≤ 1.9.x (1.10+ needs Kotlin 2.3)

## File Layout

```
library/src/
├── commonMain/kotlin/com/cikup/sumopod/ai/
│   ├── SumoPodAI.kt          # Public interface
│   ├── SumoPodAIClient.kt     # Implementation
│   ├── SumoPodConfig.kt       # Config + DSL builder
│   ├── model/                 # Request/response data classes
│   ├── error/                 # SumoPodException sealed hierarchy
│   ├── cache/                 # Room database (opt-in)
│   ├── internal/              # HttpClientFactory, SseParser, InputValidator
│   └── di/                    # Koin module
├── jvmMain/     → CIO engine
├── androidMain/ → OkHttp engine + Room builder
└── iosMain/     → Darwin engine + Room builder
```

## Testing

Tests are in `library/src/commonTest/`. Run with `./gradlew :library:jvmTest`.

Test files: SerializationTest, ChatCompletionTest, StreamingTest, ErrorHandlingTest, SecurityTest, InputValidatorTest.
