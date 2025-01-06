package org.fasheep.fair.feature.sortition

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.fasheep.fair.core.blockchain.EthereumRepository
import javax.inject.Inject

@HiltViewModel
class SortitionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val ethereumRepository: EthereumRepository
) : ViewModel() {
    val isConnected = ethereumRepository.isConnected
    val selectAddress = ethereumRepository.selectedAddress

    fun connect() {
        viewModelScope.launch {
            ethereumRepository.connect()
        }
    }
}
