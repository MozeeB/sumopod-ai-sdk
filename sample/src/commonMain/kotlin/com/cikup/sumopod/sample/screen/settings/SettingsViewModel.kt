package com.cikup.sumopod.sample.screen.settings

import com.cikup.sumopod.ai.SumopodConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel {

    private val _apiKey = MutableStateFlow("")
    val apiKey: StateFlow<String> = _apiKey.asStateFlow()

    private val _baseUrl = MutableStateFlow(SumopodConfig.DEFAULT_BASE_URL)
    val baseUrl: StateFlow<String> = _baseUrl.asStateFlow()

    fun updateApiKey(key: String) {
        _apiKey.value = key
    }

    fun updateBaseUrl(url: String) {
        _baseUrl.value = url
    }

    val isConfigured: Boolean
        get() = _apiKey.value.isNotBlank() &&
                _apiKey.value.startsWith("sk-") &&
                _apiKey.value.length >= 20
}
