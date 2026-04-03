package com.cikup.sumopod.ai.internal

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO

internal actual fun createPlatformEngine(): HttpClientEngine = CIO.create()
