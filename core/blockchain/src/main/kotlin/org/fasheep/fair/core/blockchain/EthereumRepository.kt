package org.fasheep.fair.core.blockchain

import android.util.Log
import io.metamask.androidsdk.EthereumFlowWrapper
import io.metamask.androidsdk.EthereumMethod
import io.metamask.androidsdk.EthereumRequest
import io.metamask.androidsdk.EthereumState
import io.metamask.androidsdk.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "EthereumRepository"

private const val CONTRACT_ADDRESS = "0x338Dfda1d2b75B7132d338E81d6C0c4BfE023C98"

@Singleton
class EthereumRepository @Inject constructor(
    private val ethereum: EthereumFlowWrapper
) {
    private val _ethereumFlow: Flow<EthereumState> = ethereum.ethereumState

    val isConnected: Flow<Boolean> = _ethereumFlow.map { it.selectedAddress.isNotEmpty() }.distinctUntilChanged()
    val selectedAddress: Flow<String> = _ethereumFlow.map { it.selectedAddress }.distinctUntilChanged()
    val chainId: Flow<String> = _ethereumFlow.map { it.sessionId }.distinctUntilChanged()

    // Wrapper function to connect the dapp.
    suspend fun connect(): Boolean {
        return when (ethereum.connect()) {
            is Result.Success -> true
            is Result.Error -> false
        }
    }

    // Wrapper function call all RPC methods.
    suspend fun sendRequest(request: EthereumRequest): Result {
        return ethereum.sendRequest(request)
    }

    suspend fun getRand(seed: Long = System.currentTimeMillis()): String {
        val params: Map<String, String> = mapOf(
            "to" to CONTRACT_ADDRESS,
            "data" to String.format("0x2530c905%064x", seed)
        )
        Log.d(TAG, "getRand: " + String.format("0x2530c905%064x", seed))
        val ethereumRequest = EthereumRequest(method = EthereumMethod.ETH_CALL.value, params = listOf(params))

        return when (val result = ethereum.sendRequest(ethereumRequest)) {
            is Result.Success.Item -> {
                Log.d(TAG, "getRand: Success\nItem: ${result.value}")
                result.value
            }

            else -> {
                Log.d(TAG, "getRand: Fail")
                "Fail"
            }
        }
    }

    suspend fun tran(seed: Long = System.currentTimeMillis()): String {
        val data = String.format("0xd583e0b8%064x", seed)
        val params = mapOf(
            "from" to ethereum.selectedAddress,
            "to" to CONTRACT_ADDRESS,
            "data" to data,
            "gas" to estimateGas(data)
        )
        val request = EthereumRequest(
            method = EthereumMethod.ETH_SEND_TRANSACTION.value,
            params = listOf(params)
        )
        return when (val result = ethereum.sendRequest(request)) {
            is Result.Success.Item -> {
                Log.d(TAG, "tran: Success\nItem: ${result.value}")
                result.value
            }

            else -> {
                Log.d(TAG, "tran: Fail")
                "Fail"
            }
        }
    }

    private suspend fun estimateGas(data: String): String {
        val params = mapOf(
            "to" to CONTRACT_ADDRESS,
            "data" to data
        )
        val request = EthereumRequest(
            method = EthereumMethod.ETH_ESTIMATE_GAS.value,
            params = listOf(params)
        )
        return when (val result = ethereum.sendRequest(request)) {
            is Result.Success.Item -> {
                Log.d(TAG, "estimate: Success\nItem: ${result.value}")
                result.value
            }

            else -> {
                Log.d(TAG, "estimate: Fail")
                "0x7000"
            }
        }
    }
}
