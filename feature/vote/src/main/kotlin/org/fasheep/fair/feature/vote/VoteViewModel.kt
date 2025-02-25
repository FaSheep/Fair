package org.fasheep.fair.feature.vote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.fasheep.fair.core.blockchain.EthereumRepository
import org.fasheep.fair.core.data.repository.HistoryRepository
import javax.inject.Inject

const val TAG = "VoteVM"

@HiltViewModel
class VoteViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val ethereumRepository: EthereumRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {
    fun createVote(endTime: Long, options: List<String>) {
        viewModelScope.launch {
            ethereumRepository.createVote(endTime / 1000, options)
        }
    }
}