package org.fasheep.fair.feature.history

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.fasheep.fair.core.blockchain.EthereumRepository
import org.fasheep.fair.core.data.repository.HistoryRepository
import org.fasheep.fair.core.model.data.HistoryItem
import javax.inject.Inject

const val TAG = "HistoryVM"

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val ethereumRepository: EthereumRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    val uiState: StateFlow<HistoryUiState> =
        ethereumRepository.selectedAddress.flatMapLatest {
            historyRepository.observeHistories(it).map { item -> HistoryUiState.Shown(item) }
        }.onEach { _refreshing = false }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = HistoryUiState.Loading
        )

    val connect = ethereumRepository.isConnected.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )

    private var _refreshing by mutableStateOf(false)
    val refreshing get() = _refreshing

    fun refresh() {
        _refreshing = true
        historyRepository.update()
    }

    fun checkConnect() {
        Log.d(TAG, "checkConnect: ${connect.value}")
        if (!connect.value) {
            viewModelScope.launch {
                ethereumRepository.connect()
            }
        }
    }
}

sealed interface HistoryUiState {
    data object Loading : HistoryUiState

    data class Shown(
        val histories: List<HistoryItem>
    ) : HistoryUiState

    data object Empty : HistoryUiState
}
