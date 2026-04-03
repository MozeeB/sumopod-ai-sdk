package com.cikup.sumopod.sample

import com.cikup.sumopod.sample.di.ClientProvider
import com.cikup.sumopod.sample.screen.settings.SettingsViewModel
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ClientProviderTest {

    @Test
    fun returnsNullWhenApiKeyEmpty() {
        val settings = SettingsViewModel()
        val provider = ClientProvider(settings)
        assertNull(provider.getClient())
    }

    @Test
    fun returnsNullWhenApiKeyTooShort() {
        val settings = SettingsViewModel()
        settings.updateApiKey("sk-short")
        val provider = ClientProvider(settings)
        assertNull(provider.getClient())
    }

    @Test
    fun returnsNullWhenApiKeyWrongPrefix() {
        val settings = SettingsViewModel()
        settings.updateApiKey("invalid-prefix-long-enough")
        val provider = ClientProvider(settings)
        assertNull(provider.getClient())
    }

    @Test
    fun returnsClientWhenApiKeyValid() {
        val settings = SettingsViewModel()
        settings.updateApiKey("sk-validKeyLongEnough12345")
        val provider = ClientProvider(settings)
        val client = provider.getClient()
        assertNotNull(client)
        client.close()
    }

    @Test
    fun reflectsApiKeyChanges() {
        val settings = SettingsViewModel()
        val provider = ClientProvider(settings)

        // Initially null
        assertNull(provider.getClient())

        // After setting valid key
        settings.updateApiKey("sk-validKeyLongEnough12345")
        val client = provider.getClient()
        assertNotNull(client)
        client.close()

        // After clearing key
        settings.updateApiKey("")
        assertNull(provider.getClient())
    }

    @Test
    fun reflectsBaseUrlChanges() {
        val settings = SettingsViewModel()
        settings.updateApiKey("sk-validKeyLongEnough12345")
        settings.updateBaseUrl("https://custom.api.com/v1")
        val provider = ClientProvider(settings)
        val client = provider.getClient()
        assertNotNull(client)
        client.close()
    }
}
