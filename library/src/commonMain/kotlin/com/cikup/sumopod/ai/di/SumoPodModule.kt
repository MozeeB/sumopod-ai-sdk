package com.cikup.sumopod.ai.di

import com.cikup.sumopod.ai.SumopodAI
import com.cikup.sumopod.ai.SumopodAIClient
import com.cikup.sumopod.ai.SumopodConfig
import com.cikup.sumopod.ai.internal.HttpClientFactory
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val sumopodModule: org.koin.core.module.Module = module {
    single {
        SumopodConfig(apiKey = get(named("sumopod_api_key")))
    }
    single {
        HttpClientFactory.create(get())
    }
    single<SumopodAI> {
        SumopodAIClient(config = get(), httpClient = get())
    }
}
