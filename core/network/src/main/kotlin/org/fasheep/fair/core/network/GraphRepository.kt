package org.fasheep.fair.core.network

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import org.fasheep.fair.core.network.di.AssignClient
import org.fasheep.fair.core.network.di.RandomClient
import org.fasheep.fair.core.network.model.Assignment
import org.fasheep.fair.core.network.model.Num
import org.fasheep.fair.core.network.service1.NumByAddressQuery
import org.fasheep.fair.core.network.service1.NumByIdQuery
import org.fasheep.fair.core.network.service2.AssignByAddressQuery
import org.fasheep.fair.core.network.service2.ByIdQuery
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "GraphRepository"

@Singleton
class GraphRepository @Inject constructor(
    @RandomClient private val randomClient: ApolloClient,
    @AssignClient private val assignClient: ApolloClient
) {

    suspend fun findNumByAddress(address: String): List<Num> {
        val response = randomClient.query(NumByAddressQuery(Optional.present(address))).execute().data?.getRands?.map {
            Num(it.blockTimestamp.toString(), it.transactionHash.toString(), it.num.toString())
        }
        return response ?: emptyList()
    }

    suspend fun findNumById(transactionHash: String): Num? {
        val rands = randomClient.query(NumByIdQuery(Optional.present(transactionHash))).execute().data?.getRands
        return if (rands.isNullOrEmpty()) null else rands.first()
            .let { Num(it.blockTimestamp.toString(), it.transactionHash.toString(), it.num.toString()) }
    }

    suspend fun findAssignmentById(transactionHash: String): Assignment? {
        return assignClient.query(ByIdQuery(Optional.present(transactionHash))).execute().data?.rolesAssigned?.let {
            Assignment(it.blockTimestamp.toString(), it.transactionHash.toString(), it.names, it.roles)
        }
    }

    suspend fun findAssignmentByAddress(address: String): List<Assignment> {
        val response =
            assignClient.query(AssignByAddressQuery(Optional.present(address)))
                .execute().data?.rolesAssigneds?.map {
                    Assignment(
                        it.blockTimestamp.toString(),
                        it.transactionHash.toString(),
                        it.names,
                        it.roles
                    )
                }
        return response ?: emptyList()
    }
}
