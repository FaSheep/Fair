package org.fasheep.fair.core.network.model

import com.apollographql.apollo.mpp.currentTimeMillis

data class VoteCreate(
    val timestamp: Long = currentTimeMillis(),
    val hash: String,
    val voteId: String,
    val endTime: Long,
    val options: List<String>
) {
    internal constructor(
        timestamp: String,
        hash: String,
        voteId: String,
        endTime: Long,
        options: List<String>
    ) : this(
        timestamp.toLong() * 1000, hash,
        voteId,
        endTime,
        options
    )
}
