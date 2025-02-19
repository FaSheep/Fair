package org.fasheep.fair.feature.history

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.fasheep.fair.core.blockchain.EthereumRepository
import org.fasheep.fair.core.data.repository.HistoryRepository
import org.fasheep.fair.core.model.data.HistoryItem
import org.fasheep.fair.core.network.model.Assignment
import org.fasheep.fair.core.network.model.Num
import javax.inject.Inject


const val TAG = "HistoryVM"

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val ethereumRepository: EthereumRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _hash = MutableStateFlow("")

    val hash = _hash.asStateFlow()

    val uiState: StateFlow<HistoryUiState> =
        ethereumRepository.selectedAddress.flatMapLatest {
            historyRepository.observeHistories(it).map { item -> HistoryUiState.Shown(item) }
        }.onEach { _refreshing = false }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = HistoryUiState.Loading
        )

    private var _refreshing by mutableStateOf(false)
    val refreshing get() = _refreshing

    fun refresh() {
        _refreshing = true
        historyRepository.update()
    }

    fun onItemClick(hash: String) {
        _hash.update { hash }
    }
}

sealed interface HistoryUiState {
    data object Loading : HistoryUiState

    data class Shown(
        val histories: List<HistoryItem>
    ) : HistoryUiState

    data object Empty : HistoryUiState
}

private fun Assignment.toItem(): HistoryItem.Assignment {
    return HistoryItem.Assignment(timestamp, name, role)
}

private fun Num.toItem(): HistoryItem.Num {
    return HistoryItem.Num(timestamp, value)
}
