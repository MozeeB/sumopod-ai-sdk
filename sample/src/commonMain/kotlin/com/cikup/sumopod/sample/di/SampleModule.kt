package com.cikup.sumopod.sample.di

import com.cikup.sumopod.ai.SumoPodAI
import com.cikup.sumopod.sample.screen.chat.ChatViewModel
import com.cikup.sumopod.sample.screen.models.ModelsViewModel
import com.cikup.sumopod.sample.screen.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sampleModule = module {
    single { SettingsViewModel() }
    single { ClientProvider(get()) }
    viewModelOf(::ChatViewModel)
    viewModelOf(::ModelsViewModel)
}

class ClientProvider(private val settingsViewModel: SettingsViewModel) {

    fun getClient(): SumoPodAI? {
        val apiKey = settingsViewModel.apiKey.value
        val baseUrl = settingsViewModel.baseUrl.value
        if (apiKey.isBlank() || !apiKey.startsWith("sk-") || apiKey.length < 20) {
            return null
        }
        return SumoPodAI(apiKey) { this.baseUrl = baseUrl }
    }
}
