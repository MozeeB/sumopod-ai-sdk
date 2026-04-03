package com.cikup.sumopod.sample.screen.settings

import androidx.lifecycle.ViewModel
import com.cikup.sumopod.ai.SumopodConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel : ViewModel() {

    private val _apiKey = MutableStateFlow("")
    val apiKey: StateFlow<String> = _apiKey.asStateFlow()

    private val _baseUrl = MutableStateFlow(SumopodConfig.DEFAULT_BASE_URL)
    val baseUrl: StateFlow<String> = _baseUrl.asStateFlow()

    private val _cacheEnabled = MutableStateFlow(false)
    val cacheEnabled: StateFlow<Boolean> = _cacheEnabled.asStateFlow()

    fun updateApiKey(key: String) {
        _apiKey.value = key
    }

    fun updateBaseUrl(url: String) {
        _baseUrl.value = url
    }

    fun toggleCache() {
        _cacheEnabled.value = !_cacheEnabled.value
    }

    val isConfigured: Boolean
        get() = _apiKey.value.isNotBlank() &&
                _apiKey.value.startsWith("sk-") &&
                _apiKey.value.length >= 20
}
