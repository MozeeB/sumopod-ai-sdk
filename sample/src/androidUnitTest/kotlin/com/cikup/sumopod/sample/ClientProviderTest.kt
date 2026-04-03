package com.cikup.sumopod.sample

import com.cikup.sumopod.ai.Sumopod
import com.cikup.sumopod.sample.di.ClientProvider
import com.cikup.sumopod.sample.screen.settings.SettingsViewModel
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ClientProviderTest {

    @Test
    fun returnsFalseWhenApiKeyEmpty() {
        val settings = SettingsViewModel()
        val provider = ClientProvider(settings)
        assertFalse(provider.ensureInitialized())
    }

    @Test
    fun returnsFalseWhenApiKeyTooShort() {
        val settings = SettingsViewModel()
        settings.updateApiKey("sk-short")
        val provider = ClientProvider(settings)
        assertFalse(provider.ensureInitialized())
    }

    @Test
    fun returnsFalseWhenApiKeyWrongPrefix() {
        val settings = SettingsViewModel()
        settings.updateApiKey("invalid-prefix-long-enough")
        val provider = ClientProvider(settings)
        assertFalse(provider.ensureInitialized())
    }

    @Test
    fun returnsTrueWhenApiKeyValid() {
        val settings = SettingsViewModel()
        settings.updateApiKey("sk-validKeyLongEnough12345")
        val provider = ClientProvider(settings)
        assertTrue(provider.ensureInitialized())
        assertTrue(Sumopod.isInitialized)
        Sumopod.close()
    }

    @Test
    fun reflectsApiKeyChanges() {
        val settings = SettingsViewModel()
        val provider = ClientProvider(settings)

        assertFalse(provider.ensureInitialized())

        settings.updateApiKey("sk-validKeyLongEnough12345")
        assertTrue(provider.ensureInitialized())
        Sumopod.close()

        settings.updateApiKey("")
        assertFalse(provider.ensureInitialized())
    }
}
