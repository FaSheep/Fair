package org.fasheep.fair.core.blockchain

import io.metamask.androidsdk.EthereumFlowWrapper
import io.metamask.androidsdk.EthereumRequest
import io.metamask.androidsdk.EthereumState
import io.metamask.androidsdk.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EthereumRepository @Inject constructor(
    private val ethereum: EthereumFlowWrapper
) {
    private val _ethereumFlow: Flow<EthereumState> = ethereum.ethereumState

    val isConnected: Flow<Boolean> = _ethereumFlow.map { it.selectedAddress.isNotEmpty() }.distinctUntilChanged()
    val selectedAddress: Flow<String> = _ethereumFlow.map { it.selectedAddress }.distinctUntilChanged()
    val chainId: Flow<String> = _ethereumFlow.map { it.sessionId }.distinctUntilChanged()

    // Wrapper function to connect the dapp.
    suspend fun connect(): Result {
        return ethereum.connect()
    }

    // Wrapper function call all RPC methods.
    suspend fun sendRequest(request: EthereumRequest): Result {
        return ethereum.sendRequest(request)
    }
}