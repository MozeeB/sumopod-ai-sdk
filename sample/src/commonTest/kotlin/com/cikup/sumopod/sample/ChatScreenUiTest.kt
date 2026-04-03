@file:OptIn(ExperimentalTestApi::class)

package com.cikup.sumopod.sample

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import com.cikup.sumopod.sample.di.ClientProvider
import com.cikup.sumopod.sample.screen.chat.ChatScreen
import com.cikup.sumopod.sample.screen.chat.ChatViewModel
import com.cikup.sumopod.sample.screen.settings.SettingsViewModel
import kotlin.test.Test

class ChatScreenUiTest {

    private fun createViewModel(): ChatViewModel {
        val settings = SettingsViewModel()
        val provider = ClientProvider(settings)
        return ChatViewModel(provider)
    }

    @Test
    fun chatScreenShowsEmptyState() = runComposeUiTest {
        setContent {
            ChatScreen(createViewModel())
        }
        onNodeWithText("Sumopod AI").assertIsDisplayed()
        onNodeWithText("Start a conversation with any AI model").assertIsDisplayed()
    }

    @Test
    fun chatScreenShowsModelSelector() = runComposeUiTest {
        setContent {
            ChatScreen(createViewModel())
        }
        onNodeWithText("Model:").assertIsDisplayed()
        onNodeWithText("gpt-4o-mini").assertIsDisplayed()
    }

    @Test
    fun sendButtonDisabledWhenInputEmpty() = runComposeUiTest {
        setContent {
            ChatScreen(createViewModel())
        }
        onNodeWithTag("sendButton").assertIsNotEnabled()
    }

    @Test
    fun sendButtonEnabledWhenInputHasText() = runComposeUiTest {
        setContent {
            ChatScreen(createViewModel())
        }
        onNodeWithTag("messageInput").performTextInput("Hello")
        onNodeWithTag("sendButton").assertIsEnabled()
    }

    @Test
    fun sendingWithoutApiKeyShowsError() = runComposeUiTest {
        setContent {
            ChatScreen(createViewModel())
        }
        onNodeWithTag("messageInput").performTextInput("Hello")
        onNodeWithTag("sendButton").performClick()

        waitForIdle()

        onNodeWithText("Please configure your API key in Settings").assertIsDisplayed()
    }

    @Test
    fun clearButtonNotVisibleWhenNoMessages() = runComposeUiTest {
        setContent {
            ChatScreen(createViewModel())
        }
        onNodeWithText("Clear").assertDoesNotExist()
    }
}
