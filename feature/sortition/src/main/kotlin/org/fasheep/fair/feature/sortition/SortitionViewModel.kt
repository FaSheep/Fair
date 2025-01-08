package org.fasheep.fair.feature.sortition

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val _num: MutableStateFlow<String> = MutableStateFlow("N/A")
    val num: Flow<String> = _num

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

    fun tranRand(){
        viewModelScope.launch {
            ethereumRepository.tran()
        }
    }
}
