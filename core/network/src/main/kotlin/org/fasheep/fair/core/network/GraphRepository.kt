package org.fasheep.fair.core.network

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "GraphRepository"

@Singleton
class GraphRepository @Inject constructor(private val apolloClient: ApolloClient) {

    suspend fun findNumByAddress(address: String): List<String> {
        val response = apolloClient.query(NumByAddressQuery(Optional.present(address))).execute().data?.getRands?.map {
            it.num.toString()
        }
        return response ?: emptyList()
    }

    suspend fun findNumById(transactionHash: String): String? {
        val rands = apolloClient.query(NumByIdQuery(Optional.present(transactionHash))).execute().data?.getRands
        return if (rands.isNullOrEmpty()) null else rands.first().num.toString()
    }
}
