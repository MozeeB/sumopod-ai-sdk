package com.cikup.sumopod.ai.di

import com.cikup.sumopod.ai.SumoPodAI
import com.cikup.sumopod.ai.SumoPodAIClient
import com.cikup.sumopod.ai.SumoPodConfig
import com.cikup.sumopod.ai.internal.HttpClientFactory
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val sumoPodModule = module {
    single {
        SumoPodConfig(apiKey = get(named("sumopod_api_key")))
    }
    single {
        HttpClientFactory.create(get())
    }
    single<SumoPodAI> {
        SumoPodAIClient(config = get(), httpClient = get())
    }
}
