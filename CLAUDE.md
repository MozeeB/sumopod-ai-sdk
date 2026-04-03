# CLAUDE.md — Sumopod AI SDK

## Project Overview

Kotlin Multiplatform SDK for the Sumopod AI API (OpenAI-compatible). Single entry point via `Sumopod` singleton object. Supports Android, iOS, and JVM.

## Build Commands

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"

./gradlew :library:build              # Build library (all targets)
./gradlew :library:jvmTest            # Run JVM tests (37 tests)
./gradlew :library:testAndroidHostTest # Run Android tests (62 tests)
./gradlew :library:iosSimulatorArm64Test # Run iOS simulator tests (37 tests)
./gradlew :sample:testDebugUnitTest   # Run sample Android tests (22 tests)
./gradlew :sample:desktopTest         # Run Compose UI tests (15 tests)
./gradlew :sample:assembleDebug       # Build Android sample APK
./gradlew :sample:run                 # Run desktop sample
./gradlew :library:compileKotlinIosArm64  # Compile iOS
```

## Architecture

- **`:library`** — SDK module (single `Sumopod` entry point)
- **`:sample`** — Compose Multiplatform demo app (Android + iOS + Desktop)

## Key Patterns

- **Entry point**: `Sumopod.init("sk-xxx")` then `Sumopod.chatCompletion(...)` etc.
- **Package**: `com.cikup.sumopod.ai`
- **HTTP**: Ktor 3.3.1 (CIO/OkHttp/Darwin per platform)
- **Serialization**: kotlinx.serialization 1.9.0
- **Streaming**: SSE parser reads `ByteReadChannel` → `Flow<ChatCompletionChunk>`
- **Security**: API keys redacted in logs/toString, HTTPS-only, input validation
- **Visibility**: `explicitApi()` enforced. `Sumopod` object + data models are public. All implementation is `internal`.

## Version Constraints

Kotlin 2.2.20 requires:
- Ktor ≤ 3.3.x (3.4+ needs Kotlin 2.3)
- kotlinx-serialization ≤ 1.9.x (1.10+ needs Kotlin 2.3)

## File Layout

```
library/src/
├── commonMain/kotlin/com/cikup/sumopod/ai/
│   ├── Sumopod.kt             # Public singleton entry point
│   ├── SumopodAI.kt           # Internal interface
│   ├── SumopodAIClient.kt     # Internal implementation
│   ├── SumopodConfig.kt       # Config + DSL builder
│   ├── model/                 # Request/response data classes
│   ├── error/                 # SumopodException sealed hierarchy
│   ├── cache/                 # Room database (internal)
│   └── internal/              # HttpClientFactory, SseParser, InputValidator
├── jvmMain/     → CIO engine
├── androidMain/ → OkHttp engine
└── iosMain/     → Darwin engine
```

## Testing

Run all 173 tests:
```bash
./gradlew :library:jvmTest :library:testAndroidHostTest :library:iosSimulatorArm64Test :sample:testDebugUnitTest :sample:desktopTest
```

Test files: SerializationTest, ChatCompletionTest, StreamingTest, ErrorHandlingTest, SecurityTest, InputValidatorTest (library); ChatViewModelTest, ClientProviderTest, SettingsViewModelTest (sample unit); ChatScreenUiTest, SettingsScreenUiTest, AppNavigationUiTest (sample Compose UI).
