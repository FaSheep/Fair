package org.fasheep.fair.core.network

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GraphRepository @Inject constructor(private val apolloClient: ApolloClient) {

    suspend fun findNumByAddress(address: String): List<String> {
        val response = apolloClient.query(NumByAddressQuery(Optional.present(address))).execute().data?.getRands?.map {
            it.num.toString()
        }
        return response ?: emptyList()
    }
}
