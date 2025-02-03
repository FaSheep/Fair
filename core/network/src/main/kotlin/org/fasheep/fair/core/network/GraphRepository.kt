package org.fasheep.fair.core.network

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import org.fasheep.fair.core.network.di.AssignClient
import org.fasheep.fair.core.network.di.RandomClient
import org.fasheep.fair.core.network.service1.NumByAddressQuery
import org.fasheep.fair.core.network.service1.NumByIdQuery
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "GraphRepository"

@Singleton
class GraphRepository @Inject constructor(
    @RandomClient private val randomClient: ApolloClient,
    @AssignClient private val assignClient: ApolloClient
) {

    suspend fun findNumByAddress(address: String): List<String> {
        val response = randomClient.query(NumByAddressQuery(Optional.present(address))).execute().data?.getRands?.map {
            it.num.toString()
        }
        return response ?: emptyList()
    }

    suspend fun findNumById(transactionHash: String): String? {
        val rands = randomClient.query(NumByIdQuery(Optional.present(transactionHash))).execute().data?.getRands
        return if (rands.isNullOrEmpty()) null else rands.first().num.toString()
    }
}
