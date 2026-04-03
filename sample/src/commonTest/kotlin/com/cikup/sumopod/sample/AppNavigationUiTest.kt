@file:OptIn(ExperimentalTestApi::class)

package com.cikup.sumopod.sample

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

class AppNavigationUiTest {

    @Test
    fun appStartsOnChatScreen() = runComposeUiTest {
        setContent { App() }
        onNodeWithText("Sumopod AI").assertIsDisplayed()
        onNodeWithText("Chat").assertIsDisplayed()
        onNodeWithText("Models").assertIsDisplayed()
        onNodeWithText("Settings").assertIsDisplayed()
    }

    @Test
    fun navigateToSettingsScreen() = runComposeUiTest {
        setContent { App() }
        onNodeWithText("Settings").performClick()
        waitForIdle()
        onNodeWithText("API Configuration").assertIsDisplayed()
    }

    @Test
    fun navigateToModelsScreen() = runComposeUiTest {
        setContent { App() }
        onNodeWithText("Models").performClick()
        waitForIdle()
        onNodeWithText("Available Models").assertIsDisplayed()
    }

    @Test
    fun navigateBackToChat() = runComposeUiTest {
        setContent { App() }

        onNodeWithText("Settings").performClick()
        waitForIdle()
        onNodeWithText("API Configuration").assertIsDisplayed()

        onNodeWithText("Chat").performClick()
        waitForIdle()
        onNodeWithText("Sumopod AI").assertIsDisplayed()
    }
}
