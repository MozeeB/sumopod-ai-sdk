# Changelog

All notable changes to this project will be documented in this file.

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
- Consumer ProGuard rules for Android consumers
- GitHub Actions CI/CD (build + test + publish to Maven Central)
- Compose Multiplatform sample app with Chat, Models, and Settings screens
