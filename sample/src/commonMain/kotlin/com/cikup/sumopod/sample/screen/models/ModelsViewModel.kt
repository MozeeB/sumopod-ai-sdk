package com.cikup.sumopod.sample.screen.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cikup.sumopod.ai.Sumopod
import com.cikup.sumopod.ai.model.ModelInfo
import com.cikup.sumopod.sample.di.ClientProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ModelsUiState(
    val models: List<ModelInfo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

class ModelsViewModel(
    private val clientProvider: ClientProvider,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ModelsUiState())
    val uiState: StateFlow<ModelsUiState> = _uiState.asStateFlow()

    fun loadModels() {
        if (!clientProvider.ensureInitialized()) {
            _uiState.update { it.copy(error = "Please configure your API key in Settings") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                val modelList = Sumopod.models()
                _uiState.update {
                    it.copy(
                        models = modelList.data.sortedBy { model -> model.id },
                        isLoading = false,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load models",
                    )
                }
            }
        }
    }
}
