package com.cikup.sumopod.sample.screen.chat

import com.cikup.sumopod.ai.Sumopod
import com.cikup.sumopod.ai.model.ChatCompletionRequest
import com.cikup.sumopod.ai.model.ChatMessage
import com.cikup.sumopod.ai.model.ChatRole
import com.cikup.sumopod.sample.di.ClientProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiMessage(
    val role: ChatRole,
    val content: String,
    val isStreaming: Boolean = false,
)

data class ChatUiState(
    val messages: List<UiMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedModel: String = "gpt-4o-mini",
)

class ChatViewModel(
    private val clientProvider: ClientProvider,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun selectModel(model: String) {
        _uiState.update { it.copy(selectedModel = model) }
    }

    fun sendMessage(content: String) {
        if (content.isBlank()) return
        if (!clientProvider.ensureInitialized()) {
            _uiState.update { it.copy(error = "Please configure your API key in Settings") }
            return
        }

        val userMessage = UiMessage(role = ChatRole.User, content = content)
        _uiState.update {
            it.copy(
                messages = it.messages + userMessage,
                isLoading = true,
                error = null,
            )
        }

        scope.launch {
            try {
                val chatMessages = _uiState.value.messages.map { msg ->
                    ChatMessage(role = msg.role, content = msg.content)
                }
                val request = ChatCompletionRequest(
                    model = _uiState.value.selectedModel,
                    messages = chatMessages,
                    temperature = 0.7,
                )

                val assistantMessage = UiMessage(
                    role = ChatRole.Assistant,
                    content = "",
                    isStreaming = true,
                )
                _uiState.update {
                    it.copy(messages = it.messages + assistantMessage)
                }

                var fullContent = ""
                Sumopod.chatCompletionStream(request).collect { chunk ->
                    val delta = chunk.choices.firstOrNull()?.delta?.content.orEmpty()
                    fullContent += delta
                    _uiState.update { state ->
                        val updatedMessages = state.messages.toMutableList()
                        updatedMessages[updatedMessages.lastIndex] = UiMessage(
                            role = ChatRole.Assistant,
                            content = fullContent,
                            isStreaming = true,
                        )
                        state.copy(messages = updatedMessages)
                    }
                }

                _uiState.update { state ->
                    val updatedMessages = state.messages.toMutableList()
                    updatedMessages[updatedMessages.lastIndex] = UiMessage(
                        role = ChatRole.Assistant,
                        content = fullContent,
                        isStreaming = false,
                    )
                    state.copy(messages = updatedMessages, isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error",
                    )
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { ChatUiState(selectedModel = it.selectedModel) }
    }

    fun dismissError() {
        _uiState.update { it.copy(error = null) }
    }
}
