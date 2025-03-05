package org.fasheep.fair.core.network.model

import com.apollographql.apollo.mpp.currentTimeMillis

data class Num internal constructor(
    val timestamp: Long = currentTimeMillis(),
    val hash: String,
    val value: String,
    val min: String,
    val max: String
) {
    internal constructor(
        timestamp: String,
        hash: String,
        value: String,
        min: String,
        max: String
    ) : this(timestamp.toLong() * 1000, hash, value, min, max)
}
