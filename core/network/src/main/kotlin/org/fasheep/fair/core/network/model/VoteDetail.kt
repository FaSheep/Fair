package org.fasheep.fair.core.network.model

import com.apollographql.apollo.mpp.currentTimeMillis

data class VoteDetail(
    val timestamp: Long = currentTimeMillis(),
    val hash: String,
    val voter: String,
    val option: Long
) {
    internal constructor(
        timestamp: String,
        hash: String,
        voter: String,
        option: Long
    ) : this(
        timestamp.toLong() * 1000,
        hash,
        voter,
        option
    )
}
