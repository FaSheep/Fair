package org.fasheep.fair.feature.sortition

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.fasheep.fair.core.blockchain.EthereumRepository
import org.fasheep.fair.core.data.repository.HistoryRepository
import org.fasheep.fair.core.network.GraphRepository
import javax.inject.Inject

private const val TAG = "SortitionVM"

@HiltViewModel
class SortitionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val ethereumRepository: EthereumRepository,
    private val graphRepository: GraphRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    init {
        Log.d(TAG, "VM: init")
    }

    val isConnected = ethereumRepository.isConnected
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false
        )
    val selectAddress = ethereumRepository.selectedAddress
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ""
        )
    private val _num: MutableStateFlow<String> = MutableStateFlow("N/A")
    val num: StateFlow<String> = _num.asStateFlow()

    fun connect() {
        viewModelScope.launch {
            ethereumRepository.connect()
        }
    }

    fun callRand() {
        viewModelScope.launch {
            _num.value = ethereumRepository.getRand()
        }
    }

    fun tranRand(callback: (String) -> Unit) {
        viewModelScope.launch {
            val transactionHash = ethereumRepository.tran()
            _num.value = ".."
            delay(1000)
            _num.value = "..."
            delay(1000)
            if (transactionHash == null) {
                _num.value = "N/A"
                return@launch
            }
            _num.value = ".."
            var temp = graphRepository.findNumById(transactionHash)
            for (i in 1..10) {
                if (temp != null) break
                delay(1000)
                _num.value = if (i % 2 == 0) ".." else "..."
                temp = graphRepository.findNumById(transactionHash)
            }
            _num.value = temp?.value ?: "N/A"
            historyRepository.update()
            callback(transactionHash)
        }
    }
}
