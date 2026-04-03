@file:OptIn(ExperimentalTestApi::class)

package com.cikup.sumopod.sample

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.cikup.sumopod.sample.screen.settings.SettingsScreen
import com.cikup.sumopod.sample.screen.settings.SettingsViewModel
import kotlin.test.Test

class SettingsScreenUiTest {

    @Test
    fun settingsScreenShowsTitle() = runComposeUiTest {
        setContent {
            SettingsScreen(SettingsViewModel())
        }
        onNodeWithText("Settings").assertIsDisplayed()
    }

    @Test
    fun settingsScreenShowsApiConfiguration() = runComposeUiTest {
        setContent {
            SettingsScreen(SettingsViewModel())
        }
        onNodeWithText("API Configuration").assertIsDisplayed()
        onNodeWithText("API Key").assertIsDisplayed()
        onNodeWithText("Base URL").assertIsDisplayed()
    }

    @Test
    fun settingsScreenShowsAboutSection() = runComposeUiTest {
        setContent {
            SettingsScreen(SettingsViewModel())
        }
        onNodeWithText("About").assertIsDisplayed()
        onNodeWithText("Sumopod AI SDK Sample App").assertIsDisplayed()
        onNodeWithText("Version 0.1.0").assertIsDisplayed()
    }

    @Test
    fun settingsScreenShowsCacheToggle() = runComposeUiTest {
        setContent {
            SettingsScreen(SettingsViewModel())
        }
        onNodeWithText("Enable Cache").assertIsDisplayed()
    }

    @Test
    fun settingsScreenShowsDefaultBaseUrl() = runComposeUiTest {
        setContent {
            SettingsScreen(SettingsViewModel())
        }
        onNodeWithText("https://ai.sumopod.com/v1").assertIsDisplayed()
    }
}
