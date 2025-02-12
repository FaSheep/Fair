package org.fasheep.fair.core.network.model

import com.apollographql.apollo.mpp.currentTimeMillis

data class Num internal constructor(
    val timestamp: Long = currentTimeMillis(),
    val value: String
) {
    internal constructor(
        timestamp: String,
        value: String
    ) : this(timestamp.toLong() * 1000, value)
}
