package org.fasheep.fair.core.blockchain

import android.util.Log
import io.ethers.abi.AbiFunction
import io.ethers.abi.AbiType
import io.metamask.androidsdk.EthereumFlowWrapper
import io.metamask.androidsdk.EthereumMethod
import io.metamask.androidsdk.EthereumRequest
import io.metamask.androidsdk.EthereumState
import io.metamask.androidsdk.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.fasheep.fair.core.blockchain.model.RoleStruct
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "EthereumRepository"

private const val RAND_CONTRACT_ADDRESS = "0x338Dfda1d2b75B7132d338E81d6C0c4BfE023C98"
private const val ROLE_CONTRACT_ADDRESS = ""

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
            "to" to RAND_CONTRACT_ADDRESS,
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
                Log.e(TAG, "getRand: Fail")
                "Fail"
            }
        }
    }

    suspend fun requestAccount() {
        val request = EthereumRequest(method = EthereumMethod.WALLET_REQUEST_PERMISSIONS.value)
        ethereum.connectWith(request)
    }

    suspend fun tran(seed: Long = System.currentTimeMillis()): String? {
        val function = AbiFunction.parseSignature(
            "function randWithRecord(uint256 userProvidedSeed) public returns (uint256)",
        )
        val data = function.encodeCall(
            arrayOf(
                seed.toBigInteger()
            )
        ).toString()
        val params = mapOf(
            "to" to RAND_CONTRACT_ADDRESS,
            "data" to data,
            "gas" to estimateGas(RAND_CONTRACT_ADDRESS, data)
        )
        val request = EthereumRequest(
            method = EthereumMethod.ETH_SEND_TRANSACTION.value,
            params = listOf(params)
        )
        return when (val result = ethereum.connectWith(request)) {
            is Result.Success.Item -> {
                Log.d(TAG, "tran: Success\nItem: ${result.value}")
                result.value
            }

            else -> {
                Log.e(TAG, "tran: Fail")
                null
            }
        }
    }

    suspend fun assignRole(names: Collection<String>, roles: Collection<RoleStruct>): String? {
        val function = AbiFunction(
            name = "assignRoles",
            inputs = listOf(
                AbiType.Array(AbiType.String),
                AbiType.Array(
                    AbiType.Tuple.struct(RoleStruct::class, AbiType.String, AbiType.UInt(256))
                )
            ),
            outputs = emptyList()
        )

        val data = function.encodeCall(
            arrayOf(
                names.toTypedArray(),
                roles.toTypedArray()
            )
        ).toString()

        val params = mapOf(
            "to" to ROLE_CONTRACT_ADDRESS,
            "data" to data,
            "gas" to estimateGas(ROLE_CONTRACT_ADDRESS, data)
        )
        val request = EthereumRequest(
            method = EthereumMethod.ETH_SEND_TRANSACTION.value,
            params = listOf(params)
        )
        return when (val result = ethereum.connectWith(request)) {
            is Result.Success.Item -> {
                Log.d(TAG, "tran: Success\nItem: ${result.value}")
                result.value
            }

            else -> {
                Log.e(TAG, "tran: Fail")
                null
            }
        }
    }

    private suspend fun estimateGas(contractAddress: String, data: String): String {
        val params = mapOf(
            "to" to contractAddress,
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
                Log.e(TAG, "estimate: Fail")
                "0x7000"
            }
        }
    }
}
