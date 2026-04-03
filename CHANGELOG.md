# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.0] - 2026-04-03

### Added
- Initial release
- Chat completions API (streaming and non-streaming)
- Embeddings API
- Model listing API
- Kotlin Multiplatform support: Android, iOS, JVM
- SSE streaming via Kotlin Flow with `collectContent()` extension
- `chat()` shorthand extension for simple requests
- Koin dependency injection module
- Room KMP database for optional model caching
- Security: API key redaction, HTTPS-only validation, input validation
- SumoPodException sealed hierarchy for type-safe error handling
- Compose Multiplatform sample app with Chat, Models, and Settings screens
