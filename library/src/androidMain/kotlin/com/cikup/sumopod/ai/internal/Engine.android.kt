package com.cikup.sumopod.ai.internal

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

internal actual fun createPlatformEngine(): HttpClientEngine = OkHttp.create()
