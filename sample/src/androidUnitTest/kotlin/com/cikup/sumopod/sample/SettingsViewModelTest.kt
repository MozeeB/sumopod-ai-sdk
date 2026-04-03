package com.cikup.sumopod.sample

import com.cikup.sumopod.ai.SumopodConfig
import com.cikup.sumopod.sample.screen.settings.SettingsViewModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SettingsViewModelTest {

    @Test
    fun initialStateHasEmptyApiKey() {
        val vm = SettingsViewModel()
        assertEquals("", vm.apiKey.value)
    }

    @Test
    fun initialBaseUrlIsDefault() {
        val vm = SettingsViewModel()
        assertEquals(SumopodConfig.DEFAULT_BASE_URL, vm.baseUrl.value)
    }

    @Test
    fun initialCacheDisabled() {
        val vm = SettingsViewModel()
        assertFalse(vm.cacheEnabled.value)
    }

    @Test
    fun updateApiKeyChangesState() {
        val vm = SettingsViewModel()
        vm.updateApiKey("sk-newKeyForTesting12345678")
        assertEquals("sk-newKeyForTesting12345678", vm.apiKey.value)
    }

    @Test
    fun updateBaseUrlChangesState() {
        val vm = SettingsViewModel()
        vm.updateBaseUrl("https://custom.api.com/v1")
        assertEquals("https://custom.api.com/v1", vm.baseUrl.value)
    }

    @Test
    fun toggleCacheFlipsState() {
        val vm = SettingsViewModel()
        assertFalse(vm.cacheEnabled.value)
        vm.toggleCache()
        assertTrue(vm.cacheEnabled.value)
        vm.toggleCache()
        assertFalse(vm.cacheEnabled.value)
    }

    @Test
    fun isConfiguredReturnsFalseWhenEmpty() {
        val vm = SettingsViewModel()
        assertFalse(vm.isConfigured)
    }

    @Test
    fun isConfiguredReturnsFalseForShortKey() {
        val vm = SettingsViewModel()
        vm.updateApiKey("sk-short")
        assertFalse(vm.isConfigured)
    }

    @Test
    fun isConfiguredReturnsFalseForWrongPrefix() {
        val vm = SettingsViewModel()
        vm.updateApiKey("invalid-prefix-long-enough-key")
        assertFalse(vm.isConfigured)
    }

    @Test
    fun isConfiguredReturnsTrueForValidKey() {
        val vm = SettingsViewModel()
        vm.updateApiKey("sk-validKeyLongEnough12345")
        assertTrue(vm.isConfigured)
    }
}
