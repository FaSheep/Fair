package org.fasheep.fair.core.blockchain

import android.util.Log
import io.ethers.abi.AbiFunction
import io.ethers.abi.AbiType
import io.ethers.core.types.Bytes
import io.metamask.androidsdk.EthereumFlowWrapper
import io.metamask.androidsdk.EthereumMethod
import io.metamask.androidsdk.EthereumRequest
import io.metamask.androidsdk.EthereumState
import io.metamask.androidsdk.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.fasheep.fair.core.blockchain.model.Role
import org.fasheep.fair.core.blockchain.model.RoleStruct
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "EthereumRepository"

private const val RAND_CONTRACT_ADDRESS = "0xc0106Ec0Cd2Ff2bAd852cfD5bC035398092c5063"
private const val ROLE_CONTRACT_ADDRESS = "0x023F5f7b609eb349A9625FDB5A8c76e6DE2edBC0"
private const val VOTE_CONTRACT_ADDRESS = "0x94b6B68Dc7BAA5B145D28182E830692eDFa2215f"
private const val CHAIN_ID = "0xaa36a7"

// TODO: Use an interface
@Singleton
class EthereumRepository @Inject constructor(
    private val ethereum: EthereumFlowWrapper
) {
    private val _ethereumFlow: Flow<EthereumState> = ethereum.ethereumState

    val isConnected: Flow<Boolean> = _ethereumFlow.map { it.selectedAddress.isNotEmpty() }.distinctUntilChanged()
    val selectedAddress: Flow<String> = _ethereumFlow.map {
        it.selectedAddress
    }.filter { it.isNotEmpty() }.distinctUntilChanged()
    val chainIdFlow: Flow<String> = _ethereumFlow.map { it.sessionId }.distinctUntilChanged()

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

    suspend fun getRand(seed: Long = System.currentTimeMillis(), min: Long, max: Long): String {
        if (ethereum.selectedAddress.isEmpty()) ethereum.connect()
        if (ethereum.chainId != CHAIN_ID) ethereum.switchEthereumChain(CHAIN_ID)
        val params: Map<String, String> = mapOf(
            "to" to RAND_CONTRACT_ADDRESS,
            "data" to String.format("0x2530c905%064x", seed),
            "chainId" to CHAIN_ID
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

    suspend fun tran(seed: Long = System.currentTimeMillis(), min: Long, max: Long): String? {
        if (ethereum.selectedAddress.isEmpty()) ethereum.connect()
        if (ethereum.chainId != CHAIN_ID) ethereum.switchEthereumChain(CHAIN_ID)
        val function = AbiFunction.parseSignature(
            "function randWithRecord(uint256 userProvidedSeed, uint256 min, uint256 max) external returns (uint256)",
        )
        val data = function.encodeCall(
            arrayOf(
                seed.toBigInteger(),
                min.toBigInteger(),
                max.toBigInteger()
            )
        ).toString()
        val params = mapOf(
            "to" to RAND_CONTRACT_ADDRESS,
            "data" to data,
            "gas" to estimateGas(RAND_CONTRACT_ADDRESS, data),
            "chainId" to CHAIN_ID
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

    suspend fun assignRole(names: Collection<String>, roles: Collection<Role>): String? {
        if (ethereum.selectedAddress.isEmpty()) ethereum.connect()
        if (ethereum.chainId != CHAIN_ID) ethereum.switchEthereumChain(CHAIN_ID)
        val function = AbiFunction(
            name = "assignWithRecord",
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
                roles.map { it.toStruct() }.toTypedArray()
            )
        ).toString()

        val params = mapOf(
            "to" to ROLE_CONTRACT_ADDRESS,
            "data" to data,
            "gas" to estimateGas(ROLE_CONTRACT_ADDRESS, data),
            "chainId" to CHAIN_ID
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

    suspend fun createVote(endTime: Long, options: List<String>): String? {
        if (ethereum.selectedAddress.isEmpty()) ethereum.connect()
        if (ethereum.chainId != CHAIN_ID) ethereum.switchEthereumChain(CHAIN_ID)
        val function = AbiFunction(
            name = "createVote",
            inputs = listOf(
                AbiType.UInt(256),
                AbiType.Array(AbiType.String)
            ),
            outputs = emptyList()
        )

        val data = function.encodeCall(
            arrayOf(
                endTime.toBigInteger(),
                options.toTypedArray()
            )
        ).toString()

        val params = mapOf(
            "to" to VOTE_CONTRACT_ADDRESS,
            "data" to data,
            "gas" to estimateGas(VOTE_CONTRACT_ADDRESS, data),
            "chainId" to CHAIN_ID
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

    suspend fun castVote(voteId: String, option: Int): String? {
        if (ethereum.selectedAddress.isEmpty()) ethereum.connect()
        if (ethereum.chainId != CHAIN_ID) ethereum.switchEthereumChain(CHAIN_ID)
        val function = AbiFunction(
            name = "castVote",
            inputs = listOf(
                AbiType.FixedBytes(32),
                AbiType.UInt(256)
            ),
            outputs = emptyList()
        )
        val data = function.encodeCall(
            arrayOf(
                Bytes(voteId),
                option.toBigInteger()
            )
        ).toString()

        val params = mapOf(
            "to" to VOTE_CONTRACT_ADDRESS,
            "data" to data,
            "gas" to estimateGas(VOTE_CONTRACT_ADDRESS, data),
            "chainId" to CHAIN_ID
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
            "data" to data,
            "chainId" to CHAIN_ID
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
