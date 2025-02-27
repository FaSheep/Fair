package org.fasheep.fair.feature.vote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import org.fasheep.fair.core.blockchain.EthereumRepository
import org.fasheep.fair.core.data.repository.HistoryRepository
import org.fasheep.fair.core.network.GraphRepository
import org.fasheep.fair.core.network.model.Vote
import javax.inject.Inject

const val TAG = "VoteVM"

@HiltViewModel
class VoteViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val ethereumRepository: EthereumRepository,
    private val graphRepository: GraphRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<VoteUiState> = MutableStateFlow(VoteUiState.Empty)

    val uiState = _uiState.asStateFlow()

    private var currentJob: Job? = null

    fun createVote(endTime: Long, options: List<String>, callback: (String) -> Unit) {
        _uiState.update { VoteUiState.Loading }
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            delay(1500)
            val hash = ethereumRepository.createVote(endTime / 1000, options)
            if (hash == null) {
                _uiState.update { VoteUiState.Shown("Send Transaction Failed") }
                return@launch
            }
            delay(3000)
            val vote: Vote? = withTimeoutOrNull(12 * 1000) {
                repeat(10) { _ ->
                    graphRepository.findVoteCreateById(hash)?.also { return@withTimeoutOrNull it }
                    delay(1000)
                }
                null
            }
            if (vote == null) {
                _uiState.update { VoteUiState.Shown("Timeout") }
            } else {
                _uiState.update { VoteUiState.Empty }
                historyRepository.update()
                callback(hash)
            }
        }
    }

    fun cancelCurrentJob() {
        currentJob?.cancel()
    }

    fun closeDialog() {
        _uiState.update { VoteUiState.Empty }
    }
}

sealed interface VoteUiState {
    data object Empty : VoteUiState
    data object Loading : VoteUiState
    data class Shown(val message: String) : VoteUiState
}
