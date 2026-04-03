# Changelog

All notable changes to this project will be documented in this file.

## [0.1.1] - 2026-04-03

### Fixed
- Upload GPG public key to keyservers (ubuntu, openpgp, mit) before Maven Central publish
- Strip `v` prefix from tag in GitHub Release Maven coordinates

### Changed
- Enable `automaticRelease = true` for Maven Central (no manual approval needed)
- Verify `com.cikup` namespace on Maven Central

### Added
- Release automation workflow (`release.yml`): trigger via GitHub Actions with patch/minor/major version bump
- Auto-generates CHANGELOG entry, bumps version in gradle files, commits, tags, and triggers publish

## [0.1.0] - 2026-04-03

### Added
- Initial release
- `Sumopod` singleton object as single entry point for the SDK
- Chat completions API (streaming and non-streaming)
- Embeddings API
- Model listing API
- Kotlin Multiplatform support: Android, iOS, JVM
- SSE streaming via Kotlin Flow with `collectContent()` extension
- `Sumopod.chat()` shorthand for simple requests
- Room KMP database for internal model caching
- Security: API key redaction, HTTPS-only validation, input validation
- `SumopodException` sealed hierarchy for type-safe error handling
- `explicitApi()` enforced — all public API explicitly marked
- Thread-safe singleton with `@Volatile` and atomic client swap
- Consumer ProGuard rules for Android consumers
- GitHub Actions CI/CD (build + test + publish to Maven Central)
- 173 automated tests: JVM (37), Android (62+22), iOS (37), Compose UI (15)
- Compose Multiplatform sample app with Chat, Models, and Settings screens
- iOS Xcode project wrapper (`sample/iosApp/`) for simulator/device builds
