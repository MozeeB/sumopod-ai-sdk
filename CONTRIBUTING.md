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
./gradlew build                          # Full build
./gradlew :library:jvmTest               # Library JVM tests
./gradlew :library:testAndroidHostTest   # Library Android tests
./gradlew :sample:testDebugUnitTest      # Sample unit tests
./gradlew :sample:assembleDebug          # Android APK
./gradlew :sample:run                    # Desktop sample
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
4. Ensure all tests pass
5. Submit a pull request

## License

By contributing, you agree that your contributions will be licensed under the Apache License 2.0.
