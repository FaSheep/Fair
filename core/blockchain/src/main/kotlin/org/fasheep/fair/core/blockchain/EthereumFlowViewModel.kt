package org.fasheep.fair.core.blockchain

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.metamask.androidsdk.EthereumFlowWrapper
import io.metamask.androidsdk.EthereumRequest
import io.metamask.androidsdk.EthereumState
import io.metamask.androidsdk.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class EthereumFlowViewModel @Inject constructor(
    private val ethereum: EthereumFlowWrapper
): ViewModel() {

    val ethereumFlow: Flow<EthereumState> get() = ethereum.ethereumState

    // Wrapper function to connect the dapp.
    suspend fun connect(): Result {
        return ethereum.connect()
    }

    // Wrapper function call all RPC methods.
    suspend fun sendRequest(request: EthereumRequest): Result {
        return ethereum.sendRequest(request)
    }
}