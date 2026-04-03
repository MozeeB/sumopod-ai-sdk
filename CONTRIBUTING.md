# Contributing to SumoPod AI SDK

## Development Setup

1. Clone the repository
2. Open in Android Studio or IntelliJ IDEA
3. Set `JAVA_HOME` to Android Studio's JBR if needed:
   ```bash
   export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
   ```

## Building

```bash
# Full build
./gradlew build

# Run tests
./gradlew :library:jvmTest

# Build Android sample
./gradlew :sample:assembleDebug

# Run desktop sample
./gradlew :sample:run
```

## Code Style

- Follow Kotlin coding conventions
- Use immutable data classes for models
- All public API must have KDoc
- All user inputs validated via `InputValidator`
- No API keys in source code or logs

## Pull Requests

1. Fork the repository
2. Create a feature branch from `main`
3. Write tests for new functionality
4. Ensure all tests pass: `./gradlew :library:jvmTest`
5. Submit a pull request

## Version Constraints

When updating dependencies, ensure ABI compatibility with Kotlin 2.2.20:
- Ktor must be ≤ 3.3.x
- kotlinx-serialization must be ≤ 1.9.x

## License

By contributing, you agree that your contributions will be licensed under the Apache License 2.0.
