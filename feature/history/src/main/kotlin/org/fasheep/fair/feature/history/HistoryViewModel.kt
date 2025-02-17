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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.fasheep.fair.core.blockchain.EthereumRepository
import org.fasheep.fair.core.model.data.HistoryItem
import org.fasheep.fair.core.network.GraphRepository
import org.fasheep.fair.core.network.model.Assignment
import org.fasheep.fair.core.network.model.Num
import javax.inject.Inject


const val TAG = "HistoryVM"

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val ethereumRepository: EthereumRepository,
    private val graphRepository: GraphRepository
) : ViewModel() {

    private val address = ethereumRepository.selectedAddress.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ""
    )

    private val _hash = MutableStateFlow("")

    val hash = _hash.asStateFlow()

    private val _uiState: MutableStateFlow<HistoryUiState> = MutableStateFlow(
        HistoryUiState.Loading
    )

    val uiState = _uiState.asStateFlow()

    private var _refreshing by mutableStateOf(false)
    val refreshing get() = _refreshing

    init {
        Log.d(TAG, "HistoryVM: Loading")
        load()
    }

    fun refresh() {
        _refreshing = true
        load()
    }

    private fun load() {
        _uiState.update {
            HistoryUiState.Loading
        }
        viewModelScope.launch {
            if (address.value.isBlank()) ethereumRepository.connect()
            Log.d("TAG", "address: ${address.value}")
            val list = graphRepository.findNumByAddress(address.value).map { it.toItem() }
                .plus(graphRepository.findAssignmentByAddress(address.value).map { it.toItem() })
            _uiState.update {
                HistoryUiState.Shown(list.sortedByDescending { it.blockTimestamp })
            }
            _refreshing = false
        }
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
