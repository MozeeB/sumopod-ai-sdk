package com.cikup.sumopod.sample

import com.cikup.sumopod.sample.di.ClientProvider
import com.cikup.sumopod.sample.screen.chat.ChatViewModel
import com.cikup.sumopod.sample.screen.settings.SettingsViewModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ChatViewModelTest {

    @Test
    fun initialStateIsEmpty() {
        val settings = SettingsViewModel()
        val provider = ClientProvider(settings)
        val vm = ChatViewModel(provider)
        val state = vm.uiState.value
        assertTrue(state.messages.isEmpty())
        assertEquals(false, state.isLoading)
        assertNull(state.error)
        assertEquals("gpt-4o-mini", state.selectedModel)
    }

    @Test
    fun selectModelUpdatesState() {
        val settings = SettingsViewModel()
        val provider = ClientProvider(settings)
        val vm = ChatViewModel(provider)
        vm.selectModel("claude-haiku-4-5")
        assertEquals("claude-haiku-4-5", vm.uiState.value.selectedModel)
    }

    @Test
    fun sendMessageWithNoApiKeyShowsError() {
        val settings = SettingsViewModel()
        val provider = ClientProvider(settings)
        val vm = ChatViewModel(provider)
        vm.sendMessage("Hello")
        assertEquals("Please configure your API key in Settings", vm.uiState.value.error)
    }

    @Test
    fun sendBlankMessageDoesNothing() {
        val settings = SettingsViewModel()
        val provider = ClientProvider(settings)
        val vm = ChatViewModel(provider)
        vm.sendMessage("")
        assertTrue(vm.uiState.value.messages.isEmpty())
    }

    @Test
    fun sendBlankSpaceMessageDoesNothing() {
        val settings = SettingsViewModel()
        val provider = ClientProvider(settings)
        val vm = ChatViewModel(provider)
        vm.sendMessage("   ")
        assertTrue(vm.uiState.value.messages.isEmpty())
    }

    @Test
    fun clearMessagesResetsState() {
        val settings = SettingsViewModel()
        val provider = ClientProvider(settings)
        val vm = ChatViewModel(provider)
        vm.selectModel("gpt-4o")
        vm.sendMessage("test") // will add error since no API key
        vm.clearMessages()
        assertTrue(vm.uiState.value.messages.isEmpty())
        assertNull(vm.uiState.value.error)
        assertEquals("gpt-4o", vm.uiState.value.selectedModel) // model preserved
    }

    @Test
    fun dismissErrorClearsError() {
        val settings = SettingsViewModel()
        val provider = ClientProvider(settings)
        val vm = ChatViewModel(provider)
        vm.sendMessage("Hello") // triggers error
        assertEquals("Please configure your API key in Settings", vm.uiState.value.error)
        vm.dismissError()
        assertNull(vm.uiState.value.error)
    }
}
