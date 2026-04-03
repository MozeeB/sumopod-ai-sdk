# Contributing to Sumopod AI SDK

## Development Setup

1. Clone the repository
2. Open in Android Studio or IntelliJ IDEA
3. Set `JAVA_HOME` if needed:
   ```bash
   export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
   ```

## Building

```bash
./gradlew build                             # Full build
./gradlew :library:jvmTest                  # Library JVM tests (37)
./gradlew :library:testAndroidHostTest      # Library Android tests (62)
./gradlew :library:iosSimulatorArm64Test    # Library iOS tests (37)
./gradlew :sample:testDebugUnitTest         # Sample unit tests (22)
./gradlew :sample:desktopTest               # Compose UI tests (15)
./gradlew :sample:assembleDebug             # Android APK
./gradlew :sample:run                       # Desktop sample
```

## Code Style

- Single entry point: all SDK access via `Sumopod` singleton object
- `explicitApi()` enforced — use `public`, `internal`, or `private` on all declarations
- Implementation classes must be `internal`
- Data models (request/response) are `public`
- Immutable data classes for all models
- No API keys in source code or logs

## Version Constraints

When updating dependencies, ensure ABI compatibility with Kotlin 2.2.20:
- Ktor must be ≤ 3.3.x
- kotlinx-serialization must be ≤ 1.9.x

## Pull Requests

1. Fork the repository
2. Create a feature branch from `main`
3. Write tests for new functionality
4. Ensure all 173 tests pass:
   ```bash
   ./gradlew :library:jvmTest :library:testAndroidHostTest :library:iosSimulatorArm64Test :sample:testDebugUnitTest :sample:desktopTest
   ```
5. Submit a pull request

## License

By contributing, you agree that your contributions will be licensed under the Apache License 2.0.
