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

## Release Process

Releases are fully automated via GitHub Actions:

1. Go to **Actions** > **Release** > **Run workflow**
2. Select version bump type: `patch`, `minor`, or `major`
3. The workflow will:
   - Calculate the next version from `library/build.gradle.kts`
   - Update version in `library/build.gradle.kts` and `sample/build.gradle.kts`
   - Generate a CHANGELOG entry from git commits since the last tag
   - Commit, tag (`vX.Y.Z`), and push to `main`
   - Trigger the **Publish to Maven Central** workflow automatically
4. The publish workflow uploads GPG keys to keyservers, publishes to Maven Central, and creates a GitHub Release

### Required GitHub Secrets

| Secret | Description |
|---|---|
| `MAVEN_CENTRAL_USERNAME` | Maven Central (Sonatype) username |
| `MAVEN_CENTRAL_PASSWORD` | Maven Central (Sonatype) password |
| `SIGNING_KEY` | GPG private key (ASCII-armored) |
| `SIGNING_KEY_ID` | GPG key ID (short format) |
| `SIGNING_PASSWORD` | GPG key passphrase |

## License

By contributing, you agree that your contributions will be licensed under the Apache License 2.0.
